package org.example.models;

import lombok.Getter;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

@Getter
public class Game {

    //<editor-fold desc="GameInfo">
    private final Grid grid;
    private final Player player;
    private int lives;
    private boolean started = false;
    private boolean betweenWaves = true;
    //</editor-fold>
    //<editor-fold desc="WaveInfo">
    private int waveNumber = 1;
    private static final int baseWaveCount = 5;
    private static final int nextWaveMoreZombies = 5;
    private static final int zombieSpawnDelay = 12;
    private static final int gameTicks = 1000;
    private record SpawnEvent(long delayMs, int row, int hp, int speed, int bounty) {
    }
    private final List<SpawnEvent> spawnPlan = new ArrayList<>();
    private int spawnedFromPlan = 0;
    private final Random rng = new Random();
    //</editor-fold>

    public Game(int rows, int cols, int startingMoney, int lives) {
        this.grid = new Grid(rows, cols);
        this.player = new Player(grid, startingMoney);
        this.lives = lives;
        prepareWave(waveNumber);
        started = false;
        betweenWaves = true;
    }

    private boolean isGameOver() { return lives <= 0; }
    //<editor-fold desc="GameInitializing">
    private int countForWave(int wave) {
        return baseWaveCount + (wave - 1) * nextWaveMoreZombies;
    }
    private void prepareWave(int wave) {
        final int rows = grid.getRows();
        final int count = countForWave(wave);
        int hp = 20;
        int speed  = 1;
        int bounty = 5;
        spawnPlan.clear();
        spawnedFromPlan = 0;
        int[] perRow = new int[rows];
        for (int i = 0; i < count; i++) {
            int row = rng.nextInt(rows);
            long dMs = rng.nextInt(zombieSpawnDelay * 1000 + 1);
            spawnPlan.add(new SpawnEvent(dMs, row, hp, speed, bounty));
            perRow[row]++;
        }
        spawnPlan.sort(Comparator.comparingLong(e -> e.delayMs));
        grid.setSpawnPreviewCounts(perRow);
        started = false;
    }
    //</editor-fold>
    //<editor-fold desc="GameEngine">
    public void tick() {
        if (started) {
            for (var p : grid.getPlantsValue()) p.act();
            int reached = grid.advanceAllZombies();
            if (reached > 0) lives -= reached;
            int bounty = grid.collectBountiesAndCleanup();
            if (bounty > 0) player.addMoney(bounty);
            boolean allSpawned = spawnedFromPlan >= spawnPlan.size();
            boolean noneAlive  = grid.getZombies().stream().noneMatch(z -> !z.isDead());
            if (allSpawned && noneAlive) {
                waveNumber++;
                prepareWave(waveNumber);
                started = false;
                betweenWaves = true;
            }
        } else {
            //
        }
    }
    public void runGameWave(PrintStream out) {
        started = true;
        betweenWaves = false;
        long startTime = System.currentTimeMillis();
        long nextTick = System.currentTimeMillis()+ gameTicks;
        while (!isGameOver()) {
            long now = System.currentTimeMillis();
            long elapsed = now - startTime;
            while (spawnedFromPlan < spawnPlan.size()
                    && spawnPlan.get(spawnedFromPlan).delayMs <= elapsed) {
                var ev = spawnPlan.get(spawnedFromPlan++);
                grid.spawnZombie(ev.row, grid.getCols() - 1, ev.hp, ev.speed, ev.bounty);
                grid.decrementPreviewAtRow(ev.row);
            }
            if (now >= nextTick) {
                tick();
                nextTick += gameTicks;
                Renderer.print(getGrid(), getPlayer().money(), getLives());
            }
            if (betweenWaves) {
                out.println("Wave finished. Next wave prepared — place plants and type 'run' to start.");
                break;
            }
            try { Thread.sleep(15); } catch (InterruptedException ignored) {}
        }
    }
    //</editor-fold>

    public void startGame(InputStream inStream, PrintStream out) {
        Scanner in = new Scanner(inStream);
        Renderer.print(getGrid(), getPlayer().money(), getLives());
        while (true) {
            if (isGameOver()) { out.println("=======GAME OVER======="); break; }
            out.print("> "); out.flush();
            if (!in.hasNextLine()) break;
            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            String cmd = p[0].toLowerCase();
            try {
                switch (cmd) {
                    case "plant" -> {
                        if (p.length < 4) { out.println("plant S|P|O (row) (col"); break; }
                        String type = p[1].toLowerCase();
                        int r = Integer.parseInt(p[2]);
                        int c = Integer.parseInt(p[3]);
                        boolean ok = switch (type) {
                            case "s", "sunflower" -> getPlayer().placeSunflower(r,c);
                            case "p", "peashooter", "shooter" -> getPlayer().placePeaShooter(r,c);
                            case "o", "potato" -> getPlayer().placePotato(r,c);
                            default -> { out.println("Unknown plant. Use S, P, or O."); yield false; }
                        };
                        out.println(ok ? "Planted." : "Failed to place.");
                    }

                    case "remove" -> {
                        if (p.length < 3) { out.println("Usage: remove (row) (col)"); break; }
                        int r = Integer.parseInt(p[1]), c = Integer.parseInt(p[2]);
                        out.println(getPlayer().removePlant(r,c) ? "Removed." : "Failed to remove.");
                    }

                    case "run" -> {
                        runGameWave(out);
                        continue;
                    }

                    case "quit" -> { out.println("Quit"); return; }

                    default -> out.println("Unknown. Type 'help'.");
                }
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }
            Renderer.print(getGrid(), getPlayer().money(), getLives());
        }
    }
}
