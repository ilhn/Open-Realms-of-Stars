package org.openRealmOfStars.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.openRealmOfStars.AI.Mission.Mission;
import org.openRealmOfStars.AI.Mission.MissionPhase;
import org.openRealmOfStars.AI.Mission.MissionType;
import org.openRealmOfStars.ambient.Bridge;
import org.openRealmOfStars.ambient.BridgeCommandType;
import org.openRealmOfStars.ambient.BridgeStatusType;
import org.openRealmOfStars.audio.music.MusicFileInfo;
import org.openRealmOfStars.audio.music.MusicPlayer;
import org.openRealmOfStars.audio.soundeffect.SoundPlayer;
import org.openRealmOfStars.game.States.AITurnView;
import org.openRealmOfStars.game.States.AmbientLightView;
import org.openRealmOfStars.game.States.BattleView;
import org.openRealmOfStars.game.States.CreditsView;
import org.openRealmOfStars.game.States.DiplomacyView;
import org.openRealmOfStars.game.States.EspionageMissionView;
import org.openRealmOfStars.game.States.EspionageView;
import org.openRealmOfStars.game.States.FleetTradeView;
import org.openRealmOfStars.game.States.FleetView;
import org.openRealmOfStars.game.States.GalaxyCreationView;
import org.openRealmOfStars.game.States.HelpView;
import org.openRealmOfStars.game.States.HistoryView;
import org.openRealmOfStars.game.States.LeaderView;
import org.openRealmOfStars.game.States.LoadGameView;
import org.openRealmOfStars.game.States.MainMenu;
import org.openRealmOfStars.game.States.NewsCorpView;
import org.openRealmOfStars.game.States.OptionsView;
import org.openRealmOfStars.game.States.PlanetBombingView;
import org.openRealmOfStars.game.States.PlanetListView;
import org.openRealmOfStars.game.States.PlanetView;
import org.openRealmOfStars.game.States.PlayerSetupView;
import org.openRealmOfStars.game.States.RealmView;
import org.openRealmOfStars.game.States.ResearchView;
import org.openRealmOfStars.game.States.SaveGameNameView;
import org.openRealmOfStars.game.States.ShipDesignView;
import org.openRealmOfStars.game.States.ShipUpgradeView;
import org.openRealmOfStars.game.States.ShipView;
import org.openRealmOfStars.game.States.StarMapView;
import org.openRealmOfStars.game.States.StatView;
import org.openRealmOfStars.game.States.VoteView;
import org.openRealmOfStars.game.config.ConfigFile;
import org.openRealmOfStars.game.config.ConfigLine;
import org.openRealmOfStars.game.tutorial.HelpLine;
import org.openRealmOfStars.game.tutorial.TutorialList;
import org.openRealmOfStars.gui.icons.Icons;
import org.openRealmOfStars.gui.mapPanel.PopupPanel;
import org.openRealmOfStars.gui.panels.BlackPanel;
import org.openRealmOfStars.gui.scrollPanel.SpaceScrollBarUI;
import org.openRealmOfStars.gui.utilies.GuiStatics;
import org.openRealmOfStars.mapTiles.FleetTileInfo;
import org.openRealmOfStars.mapTiles.Tile;
import org.openRealmOfStars.mapTiles.TileNames;
import org.openRealmOfStars.mapTiles.anomaly.AnomalyType;
import org.openRealmOfStars.mapTiles.anomaly.SpaceAnomaly;
import org.openRealmOfStars.player.AiDifficulty;
import org.openRealmOfStars.player.PlayerColor;
import org.openRealmOfStars.player.PlayerInfo;
import org.openRealmOfStars.player.PlayerList;
import org.openRealmOfStars.player.SpaceRace.SpaceRace;
import org.openRealmOfStars.player.combat.Combat;
import org.openRealmOfStars.player.diplomacy.Diplomacy;
import org.openRealmOfStars.player.diplomacy.DiplomacyBonusList;
import org.openRealmOfStars.player.diplomacy.DiplomaticTrade;
import org.openRealmOfStars.player.diplomacy.negotiation.NegotiationType;
import org.openRealmOfStars.player.fleet.Fleet;
import org.openRealmOfStars.player.fleet.FleetVisibility;
import org.openRealmOfStars.player.fleet.TradeRoute;
import org.openRealmOfStars.player.government.GovernmentType;
import org.openRealmOfStars.player.leader.Job;
import org.openRealmOfStars.player.leader.LeaderUtility;
import org.openRealmOfStars.player.leader.stats.StatType;
import org.openRealmOfStars.player.message.ChangeMessage;
import org.openRealmOfStars.player.message.ChangeMessageFleet;
import org.openRealmOfStars.player.message.ChangeMessagePlanet;
import org.openRealmOfStars.player.message.Message;
import org.openRealmOfStars.player.message.MessageType;
import org.openRealmOfStars.player.ship.Ship;
import org.openRealmOfStars.player.ship.ShipComponent;
import org.openRealmOfStars.player.ship.ShipComponentFactory;
import org.openRealmOfStars.player.ship.ShipHull;
import org.openRealmOfStars.player.ship.ShipHullFactory;
import org.openRealmOfStars.player.ship.ShipStat;
import org.openRealmOfStars.player.ship.shipdesign.ShipDesign;
import org.openRealmOfStars.player.tech.Tech;
import org.openRealmOfStars.player.tech.TechFactory;
import org.openRealmOfStars.player.tech.TechType;
import org.openRealmOfStars.starMap.Coordinate;
import org.openRealmOfStars.starMap.CulturePower;
import org.openRealmOfStars.starMap.GalaxyConfig;
import org.openRealmOfStars.starMap.PirateDifficultLevel;
import org.openRealmOfStars.starMap.Route;
import org.openRealmOfStars.starMap.StarMap;
import org.openRealmOfStars.starMap.StarMapUtilities;
import org.openRealmOfStars.starMap.history.event.EventOnPlanet;
import org.openRealmOfStars.starMap.history.event.EventType;
import org.openRealmOfStars.starMap.newsCorp.ImageInstruction;
import org.openRealmOfStars.starMap.newsCorp.NewsCorpData;
import org.openRealmOfStars.starMap.newsCorp.NewsData;
import org.openRealmOfStars.starMap.newsCorp.NewsFactory;
import org.openRealmOfStars.starMap.planet.BuildingFactory;
import org.openRealmOfStars.starMap.planet.Planet;
import org.openRealmOfStars.starMap.planet.construction.Building;
import org.openRealmOfStars.utilities.DiceGenerator;
import org.openRealmOfStars.utilities.ErrorLogger;
import org.openRealmOfStars.utilities.GenericFileFilter;
import org.openRealmOfStars.utilities.repository.ConfigFileRepository;
import org.openRealmOfStars.utilities.repository.GameRepository;

/**
 * Open Realm of Stars game project
 * Copyright (C) 2016-2021 Tuomo Untinen
 * Copyright (C) 2017 God Beom
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/
 *
 *
 * Contains runnable main method which runs the Game class.
 *
 */

public class Game implements ActionListener {

  /**
   * Game Title show in various places
   */
  public static final String GAME_TITLE = "Open Realm of Stars";

  /**
   * Game version number
   */
  public static final String GAME_VERSION = "0.19.29Beta";

  /**
   * Animation timer used for animation
   */
  private Timer animationTimer;

  /**
   * Music timer used for music
   */
  private Timer musicTimer;

  /**
   * Star map for the game
   */
  private StarMap starMap = null;

  /**
   * List of players
   */
  private PlayerList players;

  /**
   * Current Game state
   */
  private GameState gameState;

  /**
   * Previous Game state
   */
  private GameState previousState;

  /**
   * Planet view Panel and handling planet
   */
  private PlanetView planetView;

  /**
   * Espionage mission view.
   */
  private EspionageMissionView espionageMissionView;

  /**
   * Planet bombing view Panel
   */
  private PlanetBombingView planetBombingView;

  /**
   * Fleet view Panel and handling the fleet
   */
  private FleetView fleetView;

  /**
   * Ship upgrade view.
   */
  private ShipUpgradeView shipUpgradeView;
  /**
   * Fleet trade view Panel and handling the trade
   */
  private FleetTradeView fleetTradeView;

  /**
   * Main menu for the game
   */
  private MainMenu mainMenu;

  /**
   * Galaxy Creation view
   */
  private GalaxyCreationView galaxyCreationView;

  /**
   * Player Setup view
   */
  private PlayerSetupView playerSetupView;

  /**
   * Save Game View
   */
  private SaveGameNameView saveGameView;

  /**
   * HelpView
   */
  private HelpView helpView;

  /**
   * Load Game View
   */
  private LoadGameView loadGameView;

  /**
   * AI Turn view
   */
  private AITurnView aiTurnView;

  /**
   * Credits for the game
   */
  private CreditsView creditsView;

  /**
   * StarMap view for the game
   */
  private StarMapView starMapView;

  /**
   * Combat view for the game
   */
  private BattleView combatView;

  /**
   * Research view for the game
   */
  private ResearchView researchView;

  /**
   * Diplomacy view for the game
   */
  private DiplomacyView diplomacyView;

  /**
   * Ship view for the game
   */
  private ShipView shipView;

  /**
   * Stat view for the game
   */
  private StatView statView;

  /**
   * Vote view for the game
   */
  private VoteView voteView;

  /**
   * Ship design view for the game
   */
  private ShipDesignView shipDesignView;

  /**
   * Galaxy config for new game
   */
  private GalaxyConfig galaxyConfig;

  /**
   * News corporation view for the game
   */
  private NewsCorpView newsCorpView;

  /**
   * Espionage view for the game
   */
  private EspionageView espionageView;

  /**
   * History view for the game
   */
  private HistoryView historyView;

  /**
   * Options view for the game
   */
  private OptionsView optionsView;
  /**
   * Realm view for showing all realm information
   */
  private RealmView realmView;
  /**
   * Realm view for showing all realm information
   */
  private LeaderView leaderView;
  /**
   * Planet List view for showing all planets realm has
   */
  private PlanetListView planetListView;
  /**
   * Options view for the game
   */
  private AmbientLightView ambientLightsView;

  /**
   * Change Message Fleet or Planet
   */
  private ChangeMessage changeMessage;

  /**
   * Separate game frame to support headless running of the game class.
   */
  private JFrame gameFrame;

  /**
   * Layered Panel for view
   */
  private JLayeredPane layeredPane;

  /**
   * Song information to draw
   */
  private JLabel songText;

  /**
   * Game ConfigFile
   */
  private ConfigFile configFile;

  /**
   * Show minimap flag for the whole game process.
   * This will not be saved.
   */
  private boolean showMiniMapFlag;

  /**
   * Save filename.
   */
  private String saveFilename;

  /**
   * Tutorial list of helps.
   */
  private static TutorialList tutorialList;
  /**
   * Flag if main method has called.
   */
  private static boolean mainMethodCalled;
  /**
   * Ambient light bridge
   */
  private Bridge bridge;
  /**
   * Get Star map
   * @return StarMap
   */
  public StarMap getStarMap() {
    return starMap;
  }

  /**
   * Game window X size aka width
   */
  private static final int WINDOW_X_SIZE = 1024;

  /**
   * Game window Y size aka height
   */
  private static final int WINDOW_Y_SIZE = 768;

  /**
   * Y coordinate when showing music text at top of screen
   */
  private static final int MUSIC_TEXT_TOP = 25;

  /**
   * Y coordinate when showing music text at bottom of screen
   */
  private static final int MUSIC_TEXT_BOTTOM = 268;

  /**
   * Animation timer delay in milli seconds.
   */
  private static final int ANIMATION_TIMER_DELAY = 75;

  /**
   * Music timer delay in milli seconds. How often music playing is about
   * to check.
   */
  private static final int MUSIC_TIMER_DELAY = 500;

  /**
   * Animation timer delay in milli seconds for credits
   */
  private static final int ANIMATION_DELAY_CREDITS = 30;

  /**
   * Animation timer delay in milli seconds for combat
   */
  private static final int ANIMATION_DELAY_COMBAT = 60;

  /**
   * Has fullscreen enabled or not.
   */
  private boolean fullscreenMode = false;
  /**
   * Constructor of Game class
   * @param visible Is game actually visible or not
   */
  public Game(final boolean visible) {
    readConfigFile();
    setShowMiniMapFlag(false);
    int musicVolume = configFile.getMusicVolume();
    int soundVolume = configFile.getSoundVolume();
    int resolutionWidth = configFile.getResolutionWidth();
    int resolutionHeight = configFile.getResolutionHeight();
    if (visible) {
      if (configFile.isHardwareAcceleration()) {
        System.setProperty("sun.java2d.opengl", "true");
      }
      gameFrame = new JFrame();
      try {
        readTutorial(null);
      } catch (IOException e1) {
        ErrorLogger.log(e1);
      }
      // Set look and feel match on CrossPlatform Look and feel
      try {
      UIManager
            .setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      UIManager.put("ScrollBarUI", SpaceScrollBarUI.class.getName());
      UIManager.put("Tree.paintLines", false);
      UIManager.put("Tree.line", GuiStatics.COLOR_GREEN_TEXT_DARK);
      UIManager.put("Tree.closedIcon", Icons.getIconByName(
          Icons.ICON_CLOSED).getAsIcon());
      UIManager.put("Tree.openIcon", Icons.getIconByName(
          Icons.ICON_AIRLOCK_OPEN).getAsIcon());
      UIManager.put("Tree.expandedIcon", Icons.getIconByName(
          Icons.ICON_EXPANDED).getAsIcon());
      UIManager.put("Tree.collapsedIcon", Icons.getIconByName(
          Icons.ICON_COLLAPSED).getAsIcon());
      UIManager.put("Tree.leafIcon", Icons.getIconByName(
          Icons.ICON_ARROW_RIGHT).getAsIcon());
      UIManager.put("Tree.background", Color.BLACK);
      UIManager.put("Tree.selectionBackground",
          GuiStatics.COLOR_DEEP_SPACE_PURPLE);
      UIManager.put("Tree.selectionForeground",
          GuiStatics.COLOR_GREEN_TEXT);
      UIManager.put("Tree.selectionBorderColor",
          GuiStatics.COLOR_DEEP_SPACE_PURPLE_DARK);
      UIManager.put("Tree.textBackground", Color.BLACK);
      UIManager.put("Tree.textForeground", GuiStatics.COLOR_GREEN_TEXT);
      UIManager.put("ToolTip.background",
          GuiStatics.COLOR_COOL_SPACE_BLUE_DARK);
      UIManager.put("ToolTip.foreground",
          GuiStatics.COLOR_COOL_SPACE_BLUE);
      UIManager.put("ToolTip.border", BorderFactory
          .createLineBorder(GuiStatics.COLOR_COOL_SPACE_BLUE_DARKER));
      gameFrame.setTitle(GAME_TITLE + " " + GAME_VERSION);
      ArrayList<BufferedImage> icons = new ArrayList<>();
      icons.add(GuiStatics.LOGO32);
      icons.add(GuiStatics.LOGO48);
      icons.add(GuiStatics.LOGO64);
      icons.add(GuiStatics.LOGO128);
      gameFrame.setIconImages(icons);
      gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      gameFrame.addWindowListener(new GameWindowListener(this));
      gameFrame.setMinimumSize(new Dimension(WINDOW_X_SIZE, WINDOW_Y_SIZE));
      gameFrame.setSize(resolutionWidth, resolutionHeight);
      gameFrame.getContentPane().setSize(resolutionWidth, resolutionHeight);
      gameFrame.setPreferredSize(new Dimension(resolutionWidth,
          resolutionHeight));
      gameFrame.setLocationRelativeTo(null);
      animationTimer = new Timer(ANIMATION_TIMER_DELAY, this);
      animationTimer.setActionCommand(GameCommands.COMMAND_ANIMATION_TIMER);
      animationTimer.start();
      musicTimer = new Timer(MUSIC_TIMER_DELAY, this);
      musicTimer.setActionCommand(GameCommands.COMMAND_MUSIC_TIMER);
      gameFrame.setUndecorated(configFile.getBorderless());
      GuiStatics.setLargerFonts(configFile.getLargerFonts());
      GraphicsDevice graphicsDevice = GraphicsEnvironment
          .getLocalGraphicsEnvironment().getDefaultScreenDevice();
      fullscreenMode = false;
      if (graphicsDevice.isFullScreenSupported() && configFile.isFullscreen()) {
        fullscreenMode = true;
        gameFrame.setResizable(true);
        gameFrame.setUndecorated(true);
        graphicsDevice.setFullScreenWindow(gameFrame);
      } else {
        gameFrame.setResizable(false);
      }
      gameFrame.setVisible(true);
      if (!isFullscreenMode()) {
        // Adjusting JFrame size. Some OS take UI component space
        // from JFrame. This happens at least with Windows 7/10 and Java8.
        int sizeX = resolutionWidth - gameFrame.getContentPane().getWidth();
        int sizeY = resolutionHeight - gameFrame.getContentPane().getHeight();
        if (sizeX > 0 || sizeY > 0) {
          ErrorLogger.log("Adjust frame, since OS's UI component require"
              + " their own space from JFrame.");
          ErrorLogger.log("Adjusting X: " + sizeX + " Adjusting Y: " + sizeY);
          gameFrame.setVisible(false);
          gameFrame.setSize(new Dimension(resolutionWidth + sizeX,
              resolutionHeight + sizeY));
          gameFrame.setMinimumSize(new Dimension(WINDOW_X_SIZE + sizeX,
              WINDOW_Y_SIZE + sizeY));
          gameFrame.setVisible(true);
        }
      }
      // Add new KeyEventDispatcher
      KeyboardFocusManager kfm = KeyboardFocusManager
          .getCurrentKeyboardFocusManager();
      kfm.addKeyEventDispatcher(new GameKeyAdapter(this));
      MusicPlayer.play(MusicPlayer.MILLION_LIGHT_YEARS);
      MusicPlayer.setLoop(false);
      MusicPlayer.setVolume(musicVolume);
      SoundPlayer.setSoundVolume(soundVolume);
      musicTimer.start();
      initBridge();
    }
    changeGameState(GameState.MAIN_MENU);
  }

  /**
   * Has Fullscreen mode enabled or not.
   * @return True if enabled.
   */
  public boolean isFullscreenMode() {
    return fullscreenMode;
  }
  /**
   * Tries to init bridge
   */
  public void initBridge() {
    if (configFile.getBridgeId() != null) {
      this.bridge = new Bridge(configFile.getBridgeHost());
      this.bridge.setUsername(configFile.getBridgeUsername());
      this.bridge.setCenterLightName(configFile.getCenterLight());
      this.bridge.setLeftLightName(configFile.getLeftLight());
      this.bridge.setRightLightName(configFile.getRightLight());
      this.bridge.setIntense(configFile.getLightIntense());
      this.bridge.setLightsEnabled(configFile.getAmbientLights());
      this.bridge.setId(configFile.getBridgeId());
      try {
        this.bridge.updateAllLights();
      } catch (IOException e) {
        this.bridge.setLightsEnabled(false);
        ErrorLogger.log(e);
      }
    }
  }

  /**
   * Set Next bridge command
   * @param command Next bridge command
   * @return true if command was set for bridge
   */
  public boolean setBridgeCommand(final BridgeCommandType command) {
    if (bridge != null && bridge.getStatus() == BridgeStatusType.CONNECTED
        && bridge.areLightsEnabled()) {
      bridge.setNextCommand(command);
      return true;
    }
    return false;
  }
  /**
   * Read Tutorial information to list.
   * @param filename Filename to tutorial, null to read default tutorial.
   * @throws IOException IOException if reading fails.
   */
  public static void readTutorial(final String filename) throws IOException {
    tutorialList = new TutorialList();
    InputStream is;
    if (filename == null) {
      is = Game.class.getClassLoader().getResource(
          "resources/tutorial.txt").openStream();
    } else {
      is = new FileInputStream(new File(filename));
    }
    try (BufferedInputStream bis = new BufferedInputStream(is)) {
      // Two megabyte buffer
      int bufferSize = 2 * 1024 * 1024;
      byte[] buffer = new byte[bufferSize];
      int offset = 0;
      int readAmount = 0;
      int left = bufferSize;
      do {
        readAmount = bis.read(buffer, offset, left);
        if (readAmount > 0) {
          offset = offset + readAmount;
          left = left - readAmount;
        }
      } while (left > 0 && readAmount > 1);
      String strBuffer = new String(buffer, 0, offset, StandardCharsets.UTF_8);
      String[] lines = strBuffer.split("\n");
      for (String line : lines) {
        HelpLine helpLine = HelpLine.parseHelpline(line);
        if (helpLine != null) {
          tutorialList.add(helpLine);
        }
      }
    }
  }
  /**
   * Read config file from oros.cfg or generate
   * default config file.
   */
  private void readConfigFile() {
    File file = new File("oros.cfg");
    if (file.exists()) {
      try (FileInputStream fis = new FileInputStream(file)) {
        configFile = ConfigFileRepository.readConfigFile(fis);
      } catch (IOException e) {
        ErrorLogger.log("Failed reading the config file! \n"
           + e.getMessage());
      }
    }
    if (configFile == null) {
      configFile = new ConfigFile();
      configFile.add(new ConfigLine(ConfigFile.CONFIG_COMMENT));
      ConfigLine line = new ConfigLine(ConfigFile.CONFIG_MUSIC_VOLUME + "=75");
      configFile.add(line);
      line = new ConfigLine(ConfigFile.CONFIG_SOUND_VOLUME + "=75");
      configFile.add(line);
      line = new ConfigLine(ConfigFile.CONFIG_RESOLUTION + "="
          + WINDOW_X_SIZE + "x" + WINDOW_Y_SIZE);
      configFile.add(line);
    }
  }

  /**
   * Write config file to oros.cfg.
   */
  private void writeConfigFile() {
    File file = new File("oros.cfg");
    try (FileOutputStream fos = new FileOutputStream(file)) {
      ConfigFileRepository.writeConfigFile(fos, configFile);
    } catch (IOException e) {
      ErrorLogger.log("Failed reading the config file! \n"
         + e.getMessage());
    }
  }

  /**
   * Get conflicting realms.
   * @param info Player who owns the fleet
   * @param fleet Fleet to move
   * @param nx New coordinate x axel
   * @param ny new coordinate y axel
   * @return PlayerInfo or null if no conflict
   */
  public PlayerInfo getConflictingRealm(final PlayerInfo info,
      final Fleet fleet, final int nx, final int ny) {
    // Getting fleet owner information
    FleetTileInfo[][] fleetTiles = starMap.getFleetTiles();
    FleetTileInfo fleetTile = fleetTiles[fleet.getX()][fleet.getY()];

    // And making sure that fleet owner is actually make the move
    boolean isSamePlayer = false;
    if (fleetTile != null) {
      isSamePlayer = players.getIndex(info) == fleetTile.getPlayerIndex();
      if (!isSamePlayer) {
        isSamePlayer = players.getIndex(info) == fleetTile.getConflictIndex();
      }
    }
    final boolean isValidCoordinate = getStarMap().isValidCoordinate(nx, ny);
    final boolean isMovesLeft = fleet.getMovesLeft() > 0;
    final boolean isNotBlocked = !getStarMap().isBlocked(nx, ny);

    if (isSamePlayer && isValidCoordinate && isMovesLeft && isNotBlocked
        && fleetTiles[nx][ny] != null) {
      if (fleet.isPrivateerFleet()) {
        return null;
      }
      fleetTile = fleetTiles[nx][ny];
      int playerIndex = fleetTile.getPlayerIndex();
      PlayerInfo info2 = players.getPlayerInfoByIndex(playerIndex);
      if (info2 == null) {
        Planet planet = getStarMap().getPlanetList().get(
            fleetTile.getPlanetIndex());
        if (planet != null
            && planet.getPlanetPlayerInfo() != null
            && info != planet.getPlanetPlayerInfo()) {
          return planet.getPlanetPlayerInfo();
        }
      } else {
        Fleet enemy = info2.getFleets().getByIndex(fleetTile.getFleetIndex());
        if (info != info2 && enemy != null) {
          if (enemy.isPrivateerFleet()) {
            return null;
          }
          if (info.getDiplomacy().isWar(playerIndex) || info2.isBoard()) {
            return null;
          }
          FleetVisibility visibility = new FleetVisibility(info, enemy,
              playerIndex);
          if (visibility.isFleetVisible() && visibility.isRecognized()) {
            return info2;
          }
        }
      }
    }
    return null;
  }
  /**
   * Get conflicting fleet if fleet is cloaked.
   * @param info Player who owns the fleet
   * @param fleet Fleet to move
   * @param nx New coordinate x axel
   * @param ny new coordinate y axel
   * @return PlayerInfo or null if no conflict
   */
  public Fleet getConflictingFleet(final PlayerInfo info,
      final Fleet fleet, final int nx, final int ny) {
    // Getting fleet owner information
    FleetTileInfo[][] fleetTiles = starMap.getFleetTiles();
    FleetTileInfo fleetTile = fleetTiles[fleet.getX()][fleet.getY()];

    // And making sure that fleet owner is actually make the move
    boolean isSamePlayer = false;
    if (fleetTile != null) {
      isSamePlayer = players.getIndex(info) == fleetTile.getPlayerIndex();
      if (!isSamePlayer) {
        isSamePlayer = players.getIndex(info) == fleetTile.getConflictIndex();
      }
    }
    final boolean isValidCoordinate = getStarMap().isValidCoordinate(nx, ny);
    final boolean isMovesLeft = fleet.getMovesLeft() > 0;
    final boolean isNotBlocked = !getStarMap().isBlocked(nx, ny);

    if (isSamePlayer && isValidCoordinate && isMovesLeft && isNotBlocked
        && fleetTiles[nx][ny] != null) {
      if (fleet.isPrivateerFleet()) {
        return null;
      }
      fleetTile = fleetTiles[nx][ny];
      int playerIndex = fleetTile.getPlayerIndex();
      int fleetIndex = fleetTile.getFleetIndex();
      PlayerInfo info2 = players.getPlayerInfoByIndex(playerIndex);
      if (info2 == null) {
        Planet planet = getStarMap().getPlanetList().get(
            fleetTile.getPlanetIndex());
        if (planet != null && planet.getOrbital() != null
          && planet.getPlanetPlayerInfo() != null
          && info != planet.getPlanetPlayerInfo()) {
          Fleet orbitalFleet = new Fleet(planet.getOrbital(), planet.getX(),
              planet.getY());
          orbitalFleet.setName("Orbital-" + planet.getName()
              + "-X" + planet.getX() + "-Y" + planet.getY());
          return orbitalFleet;
        }
      } else {
        Fleet enemy = info2.getFleets().getByIndex(fleetIndex);
        if (info != info2 && enemy != null) {
          if (enemy.isPrivateerFleet()) {
            return null;
          }
          if (info.getDiplomacy().isWar(playerIndex) || info2.isBoard()) {
            return null;
          }
          FleetVisibility visibility = new FleetVisibility(info, enemy,
              playerIndex);
          if (!visibility.isFleetVisible()) {
            return info2.getFleets().getByIndex(fleetIndex);
          }
        }
      }
    }
    return null;
  }
  /**
   * Cause Fleet to make a move
   * @param info Player who owns the fleet
   * @param fleet Fleet to move
   * @param nx New coordinate x axel
   * @param ny new coordinate y axel
   */
  public void fleetMakeMove(final PlayerInfo info, final Fleet fleet,
      final int nx, final int ny) {
    // Getting fleet owner information
    FleetTileInfo[][] fleetTiles = starMap.getFleetTiles();
    FleetTileInfo fleetTile = fleetTiles[fleet.getX()][fleet.getY()];

    // And making sure that fleet owner is actually make the move
    boolean isSamePlayer = false;
    if (fleetTile != null) {
      isSamePlayer = players.getIndex(info) == fleetTile.getPlayerIndex();
      if (!isSamePlayer) {
        isSamePlayer = players.getIndex(info) == fleetTile.getConflictIndex();
      }
    }
    final boolean isValidCoordinate = getStarMap().isValidCoordinate(nx, ny);
    final boolean isMovesLeft = fleet.getMovesLeft() > 0;
    final boolean isNotBlocked = !getStarMap().isBlocked(nx, ny);

    if (isSamePlayer && isValidCoordinate && isMovesLeft && isNotBlocked) {
      Combat combat = getStarMap().fightWithFleet(nx, ny, fleet, info);
      if (combat != null) {
        fleet.decMovesLeft();
        Coordinate combatCoord = combat.getCombatCoordinates();
        Planet planet = starMap.getPlanetByCoordinate(combatCoord.getX(),
            combatCoord.getY());
        combat.setPlanet(planet);
        int defenderIndex = starMap.getPlayerList().getIndex(
            combat.getPlayer2());
        int attackerIndex = starMap.getPlayerList().getIndex(
            combat.getPlayer1());
        if (!info.getDiplomacy().getDiplomaticRelation(defenderIndex).equals(
            Diplomacy.WAR) && !fleet.isPrivateerFleet()
            && !combat.getDefendingFleet().isPrivateerFleet()) {
          // Moving on towards another player's fleet is war declaration
          DiplomaticTrade trade = new DiplomaticTrade(starMap, attackerIndex,
              defenderIndex);
          trade.generateEqualTrade(NegotiationType.WAR);
          boolean casusBelli = info.getDiplomacy().hasCasusBelli(
              defenderIndex);
          StarMapUtilities.addWarDeclatingReputation(starMap, info,
              combat.getPlayer2());
          NewsData newsData = NewsFactory.makeWarNews(info,
              combat.getPlayer2(), planet, starMap, casusBelli);
          starMap.getNewsCorpData().addNews(newsData);
          starMap.getHistory().addEvent(NewsFactory.makeDiplomaticEvent(
              planet, newsData));
          trade.doTrades();
          PlayerInfo defender = starMap.getPlayerByIndex(defenderIndex);
          String[] list = defender.getDiplomacy().activateDefensivePact(
              starMap, info);
          if (list != null) {
            starMap.getNewsCorpData().addNews(
                NewsFactory.makeDefensiveActivation(info, list));
          }
        }
        if (combat.isHumanPlayer()) {
          starMapView.setReadyToMove(false);
          changeGameState(GameState.COMBAT, combat);
        } else {
          combat.doFastCombat();
          getStarMap().getHistory().addEvent(combat.getCombatEvent());
          if (combat.getLeaderKilledNews() != null
              && getStarMap().isAllNewsEnabled()) {
            getStarMap().getNewsCorpData().addNews(
                combat.getLeaderKilledNews());
          }
          if (combat.getOrbitalDestoyedNews() != null) {
            getStarMap().getNewsCorpData().addNews(
                combat.getOrbitalDestoyedNews());
          }
        }
      } else {
        fleet.setPos(new Coordinate(nx, ny));
        SpaceAnomaly anomaly = SpaceAnomaly.createAnomalyEvent(starMap,
            info, fleet);
        if (anomaly != null && fleet.getCommander() != null) {
          fleet.getCommander().getStats().addOne(StatType.NUMBER_OF_ANOMALY);
        }
        if (anomaly != null && starMapView != null && info.isHuman()) {
          if (MusicPlayer.getNowPlaying() != MusicPlayer.MYSTERIOUS_ANOMALY) {
            MusicPlayer.play(MusicPlayer.MYSTERIOUS_ANOMALY);
          }
          PopupPanel popup = new PopupPanel(anomaly);
          starMapView.setPopup(popup);
        }
        if (anomaly != null && !info.isHuman() && anomaly.getCombat() != null
            && !info.isBoard()) {
          anomaly.getCombat().doFastCombat();
          getStarMap().getHistory().addEvent(
              anomaly.getCombat().getCombatEvent());
          if (anomaly.getCombat().getLeaderKilledNews() != null
              && getStarMap().isAllNewsEnabled()) {
            getStarMap().getNewsCorpData().addNews(
                anomaly.getCombat().getLeaderKilledNews());
          }
          if (anomaly.getCombat().getOrbitalDestoyedNews() != null) {
            getStarMap().getNewsCorpData().addNews(
                anomaly.getCombat().getOrbitalDestoyedNews());
          }
        }
        if (anomaly != null && anomaly.getType() == AnomalyType.TIME_WARP
            && starMapView != null) {
          starMapView.setShowFleet(fleet);
        }
        starMap.clearFleetTiles();
        fleet.decMovesLeft();
        getStarMap().doFleetScanUpdate(info, fleet, null);
        Tile tile = getStarMap().getTile(fleet.getX(), fleet.getY());
        if (tile.getName().equals(TileNames.WORM_HOLE1)
            || tile.getName().equals(TileNames.WORM_HOLE2)) {
          Coordinate coord = getStarMap().getFreeWormHole(
              fleet.getCoordinate());
          fleet.setPos(coord);
          starMap.clearFleetTiles();
          getStarMap().doFleetScanUpdate(info, fleet, null);
          if (fleet.getCommander() != null) {
            fleet.getCommander().setExperience(
                fleet.getCommander().getExperience() + 2);
          }
        }
        if (starMapView != null) {
          starMapView.updatePanels();
          if (info.isHuman()) {
            getStarMap().setDrawPos(fleet.getX(), fleet.getY());
          }
          starMapView.setReadyToMove(false);
        }
      }
    }
  }

  /**
   * Update View
   * @param view about BlackPanel
   */
  private void updateDisplay(final BlackPanel view) {
    if (gameFrame != null) {
      gameFrame.getContentPane().removeAll();
      layeredPane = new JLayeredPane();
      view.setBounds(0, 0, getWidth(), getHeight());
      layeredPane.setLayer(view, JLayeredPane.DEFAULT_LAYER);
      layeredPane.add(view);
      layeredPane.setBounds(0, 0, getWidth(), getHeight());
      int y = MUSIC_TEXT_TOP;
      if (view instanceof MainMenu || view instanceof ResearchView
          || view instanceof ShipView
          || view instanceof PlayerSetupView
          || view instanceof DiplomacyView
          || view instanceof BattleView) {
        y = getHeight() - MUSIC_TEXT_BOTTOM;
      }
      songText = new JLabel("Test");
      songText.setBounds(getWidth() - 374, y, 350, 150);
      songText.setFont(GuiStatics.getFontSquarionBold());
      songText.setForeground(Color.white);
      MusicFileInfo info = MusicPlayer.getNowPlaying();
      String text = "<html>Now playing: " + info.getName() + " by "
          + info.getAuthor() + "</html>";
      songText.setText(text);
      if (MusicPlayer.isMusicEnabled()) {
        layeredPane.setLayer(songText, JLayeredPane.POPUP_LAYER);
        layeredPane.add(songText);
      }
      if (MusicPlayer.isTextDisplayedEnough()) {
        songText.setVisible(false);
      }
      gameFrame.add(layeredPane);
      gameFrame.validate();
    }
  }

  /**
   * Set Game frame to be resizable.
   * @param resizable True for resizable frame
   */
  public void setResizable(final boolean resizable) {
    if (gameFrame != null) {
      gameFrame.setResizable(resizable);
    }
  }

  /**
   * Set new resolution for game frame
   * @param resolution Resolution as String NNNxMMM
   */
  public void setNewResolution(final String resolution) {
    String[] resolutionParts = resolution.split("x");
    int resolutionWidth = Integer.parseInt(resolutionParts[0]);
    int resolutionHeight = Integer.parseInt(resolutionParts[1]);
    if (resolutionWidth != gameFrame.getWidth()
        || resolutionHeight != gameFrame.getHeight()) {
      gameFrame.setVisible(false);
      gameFrame.setSize(resolutionWidth, resolutionHeight);
      gameFrame.setLocationRelativeTo(null);
      gameFrame.setVisible(true);
      // Adjusting JFrame size. Some OS take UI component space
      // from JFrame. This happens at least with Windows 7/10 and Java8.
      int sizeX = gameFrame.getWidth() - gameFrame.getContentPane().getWidth();
      int sizeY = gameFrame.getHeight()
          - gameFrame.getContentPane().getHeight();
      if (sizeX > 0 || sizeY > 0) {
        ErrorLogger.log("Adjust frame, since OS's UI component require"
            + " their own space from JFrame.");
        ErrorLogger.log("Adjusting X: " + sizeX + " Adjusting Y: " + sizeY);
        gameFrame.setVisible(false);
        gameFrame.setSize(resolutionWidth + sizeX, resolutionHeight + sizeY);
        gameFrame.setMinimumSize(new Dimension(WINDOW_X_SIZE + sizeX,
            WINDOW_Y_SIZE + sizeY));
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
      }
      gameFrame.repaint();
    }
  }

  /**
   * Get Current Resolution NNNxMMM
   * @return Current ConentPane resolution as string
   */
  public String getCurrentResolution() {
    if (gameFrame != null) {
      return getWidth() + "x" + getHeight();
    }
    return "1024x768";
  }
  /**
   * Get width of Game frame
   * @return Width of game frame
   */
  public int getWidth() {
    if (gameFrame != null) {
      return gameFrame.getContentPane().getWidth();
    }
    return WINDOW_X_SIZE;
  }

  /**
   * Get height of Game frame
   * @return Width of game frame
   */
  public int getHeight() {
    if (gameFrame != null) {
      return gameFrame.getContentPane().getHeight();
    }
    return WINDOW_Y_SIZE;
  }

  /**
   * Call paint for Game frame
   * @param graphics Graphics
   */
  public void paint(final Graphics graphics) {
    if (gameFrame != null) {
      gameFrame.getContentPane().paint(graphics);
    }
  }

  /**
   * Show planet view panel
   * @param planet Planet to show
   * @param interactive Show planet view as interactive if true
   * @param player player who is currently playing
   */
  public void showPlanetView(final Planet planet, final PlayerInfo player,
      final boolean interactive) {
    planetView = new PlanetView(planet, interactive, player, this);
    this.updateDisplay(planetView);
  }

  /**
   * Show espionage view panel
   * @param planet Planet where to do espionage missions
   * @param player player who is currently playing
   */
  public void showEspionageMissionView(final Planet planet,
      final PlayerInfo player) {
    if (fleetView != null) {
      espionageMissionView = new EspionageMissionView(planet, player,
          fleetView.getFleet(), this);
      this.updateDisplay(espionageMissionView);
    }
  }

  /**
   * Show news corp view
   */
  public void showNewsCorpView() {
    newsCorpView = new NewsCorpView(starMap.getNewsCorpData().getNewsList(),
        this);
    this.updateDisplay(newsCorpView);
  }

  /**
   * Show Options view
   */
  public void showOptionsView() {
    optionsView = new OptionsView(configFile, this, this);
    this.updateDisplay(optionsView);
  }

  /**
   * Show ambient lights view
   */
  public void showAmbientLightsView() {
    ambientLightsView = new AmbientLightView(configFile, bridge, this);
    this.updateDisplay(ambientLightsView);
  }

  /**
   * Show espionage view
   */
  public void showEspionageView() {
    espionageView = new EspionageView(starMap.getPlayerList(),
        starMap.getCurrentPlayerInfo(),
        starMap.getNewsCorpData().getMilitary(), this);
    this.updateDisplay(espionageView);
  }

  /**
   * Show planet bombing view panel. Also
   * declares war against player who's planet is being
   * conquered if there is now war between these two players.
   * @param planet Planet to show
   * @param fleet Fleet to show
   */
  public void showPlanetBombingView(final Planet planet, final Fleet fleet) {
    if (!starMap.getCurrentPlayerInfo().getDiplomacy().isWar(
        planet.getPlanetOwnerIndex())) {
      // No war between these two players, trying to conquer another player's
      // planet is act of war.
      DiplomaticTrade trade = new DiplomaticTrade(starMap,
          players.getCurrentPlayer(), planet.getPlanetOwnerIndex());
      trade.generateEqualTrade(NegotiationType.WAR);
      trade.doTrades();
      PlayerInfo defender = planet.getPlanetPlayerInfo();
      PlayerInfo attacker = starMap.getCurrentPlayerInfo();
      int defenderIndex = starMap.getPlayerList().getIndex(defender);
      boolean casusbelli = attacker.getDiplomacy().hasCasusBelli(
          defenderIndex);
      StarMapUtilities.addWarDeclatingReputation(starMap, attacker, defender);
      NewsData newsData = NewsFactory.makeWarNews(attacker, defender, fleet,
          starMap, casusbelli);
      starMap.getNewsCorpData().addNews(newsData);
      starMap.getHistory().addEvent(NewsFactory.makeDiplomaticEvent(
          fleet, newsData));
      String[] defenseList = defender.getDiplomacy().activateDefensivePact(
          starMap, attacker);
      if (defenseList != null) {
        starMap.getNewsCorpData().addNews(
            NewsFactory.makeDefensiveActivation(attacker, defenseList));
      }
    }
    planetBombingView = new PlanetBombingView(planet, fleet,
        starMap.getCurrentPlayerInfo(), players.getCurrentPlayer(), this);
    planetBombingView.setStarMap(starMap);
    this.updateDisplay(planetBombingView);
  }

  /**
   * Show planet bombing view panel
   * @param view PlanetBombingView to show
   */
  public void showPlanetBombingView(final PlanetBombingView view) {
    planetBombingView = view;
    planetBombingView.setStarMap(starMap);
    this.updateDisplay(planetBombingView);
  }

  /**
   * Show fleet view panel
   * @param planet Planet to show
   * @param fleet Fleet to show
   * @param interactive If true then player can edit the fleet
   */
  public void showFleetView(final Planet planet, final Fleet fleet,
      final boolean interactive) {
    fleetView = new FleetView(planet, fleet,
        players.getCurrentPlayerInfo().getFleets(),
        players.getCurrentPlayerInfo(), interactive, this);
    fleetView.setStarmap(getStarMap());
    this.updateDisplay(fleetView);
  }

  /**
   * Show Fleet upgrade view.
   */
  public void showFleetUpgradeView() {
    if (fleetView != null) {
      shipUpgradeView = new ShipUpgradeView(players.getCurrentPlayerInfo(),
          fleetView.getFleet(), fleetView.getPlanet(), this);
      this.updateDisplay(shipUpgradeView);
    }
  }
  /**
   * Show fleet trade view panel
   * @param planet Planet to show
   * @param fleet Fleet to show
   */
  public void showFleetTradeView(final Planet planet, final Fleet fleet) {
    fleetTradeView = new FleetTradeView(starMap,
        players.getCurrentPlayerInfo(), planet, fleet, this);
    this.updateDisplay(fleetTradeView);
  }

  /**
   * Show Star Map panels
   * @param object to show on map, Currently work only with fleet.
   */
  public void showStarMap(final Object object) {
    starMapView = new StarMapView(starMap, players, this);
    this.updateDisplay(starMapView);
    starMapView.setAutoFocus(false);
    if (object == null) {
      focusOnMessage(true);
    } else if (object instanceof Fleet) {
      Fleet fleet = (Fleet) object;
      starMapView.setShowFleet(fleet);
      starMapView.getStarMapMouseListener().setLastClickedFleet(fleet);
      starMapView.getStarMapMouseListener().setLastClickedPlanet(null);
    }
  }

  /**
   * Show History view
   */
  public void showHistoryView() {
    historyView = new HistoryView(starMap, this);
    this.updateDisplay(historyView);
  }

  /**
   * Show Diplomacy view
   * @param dataObject Object where PlayerInfo with whom diplomacy
   * is going to be started is need to find
   */
  public void showDiplomacyView(final Object dataObject) {
    PlayerInfo info = starMap.getPlayerByIndex(1);
    Fleet fleet = null;
    Planet planet = null;
    int type = DiplomacyView.HUMAN_REGULAR;
    if (dataObject != null) {
      if (dataObject instanceof Fleet) {
        fleet = (Fleet) dataObject;
        info = starMap.getPlayerInfoByFleet(fleet);
        planet = starMap.getPlanetByCoordinate(fleet.getX(), fleet.getY());
        if (info.isHuman()) {
          type = DiplomacyView.AI_BORDER_CROSS;
          CulturePower culture = starMap.getSectorCulture(fleet.getX(),
              fleet.getY());
          if (culture.getHighestCulture() != -1) {
            info = starMap.getPlayerByIndex(culture.getHighestCulture());
            type = Diplomacy.getBorderCrossingType(info, fleet, 0);
          } else {
            // This should not happen, but it means
            // that AI started diplomacy not on his/her sector.
            type = DiplomacyView.AI_REGULAR;
          }
        } else {
          type = DiplomacyView.HUMAN_BORDER_CROSS;
          PlayerInfo human = starMap.getPlayerByIndex(0);
          int index = starMap.getPlayerList().getIndex(info);
          type = Diplomacy.getBorderCrossingType(human, fleet, index);
        }
      }
      if (dataObject instanceof FleetView) {
        FleetView view = (FleetView) dataObject;
        planet = view.getPlanet();
        info = starMap.getPlayerInfoByFleet(view.getFleet());
      }
      if (dataObject instanceof PlanetView) {
        PlanetView view = (PlanetView) dataObject;
        if (view.getPlanet().getPlanetPlayerInfo() != null) {
          info = view.getPlanet().getPlanetPlayerInfo();
          planet = view.getPlanet();
        }
      }
      if (dataObject instanceof Planet) {
        planet = (Planet) dataObject;
        if (planet.getPlanetPlayerInfo() != null) {
          info = planet.getPlanetPlayerInfo();
        }
      }
      if (dataObject instanceof PlayerInfo) {
        info = (PlayerInfo) dataObject;
        type = DiplomacyView.AI_REGULAR;
      }
    }
    diplomacyView = new DiplomacyView(starMap.getPlayerByIndex(0), info,
        starMap, type, fleet, planet, this);
    this.updateDisplay(diplomacyView);
  }

  /**
   * Show Combat
   * @param combat Combat to show
   */
  public void showCombat(final Combat combat) {
    if (combat == null) {
      // This is fixed combat
      combatView = new BattleView(
          players.getCurrentPlayerInfo().getFleets().getByIndex(0),
          players.getCurrentPlayerInfo(),
          players.getPlayerInfoByIndex(1).getFleets().getByIndex(0),
          players.getPlayerInfoByIndex(1), starMap, this);
    } else {
      combatView = new BattleView(combat, starMap, this);
    }
    this.updateDisplay(combatView);
  }

  /**
   * Show Research panels
   * @param focusMsg which tech should have focus on show up. Can be null.
   */
  public void showResearch(final Message focusMsg) {
    String focusTech = null;
    if (focusMsg != null && focusMsg.getType() == MessageType.RESEARCH
        && focusMsg.getMatchByString() != null) {
      focusTech = focusMsg.getMatchByString();
    }
    researchView = new ResearchView(players.getCurrentPlayerInfo(),
        starMap.getTotalProductionByPlayerPerTurn(Planet.PRODUCTION_RESEARCH,
            players.getCurrentPlayer()),
        focusTech, starMap.getScoreVictoryTurn(), this);
    this.updateDisplay(researchView);
  }

  /**
   * Show Help panels
   */
  public void showHelp() {
    helpView = new HelpView(tutorialList, starMap.isTutorialEnabled(), this);
    this.updateDisplay(helpView);
  }

  /**
   * Show Ship panels
   */
  public void showShipView() {
    shipView = new ShipView(players.getCurrentPlayerInfo(), this);
    this.updateDisplay(shipView);
  }

  /**
   * Show Stat panels
   */
  public void showStatView() {
    statView = new StatView(starMap, this);
    if (starMap.getTurn() == starMap.getScoreVictoryTurn()) {
      statView.setBackButtonText("Back to main menu");
    }
    this.updateDisplay(statView);
  }

  /**
   * Show Voting panels
   */
  public void showVoteView() {
    voteView = new VoteView(starMap, this);
    this.updateDisplay(voteView);
  }

  /**
   * Show Ship design panels
   * @param oldDesign to copy to new one. Can be null.
   */
  public void showShipDesignView(final ShipDesign oldDesign) {
    shipDesignView = new ShipDesignView(players.getCurrentPlayerInfo(),
        oldDesign, this);
    shipDesignView.setBanNukes(getStarMap().getVotes().areNukesBanned());
    shipDesignView.setBanPrivateer(
        getStarMap().getVotes().arePrivateersBanned());
    this.updateDisplay(shipDesignView);
  }

  /**
   * Show main menu panel
   */
  public void showMainMenu() {
    mainMenu = new MainMenu(this, null);
    this.updateDisplay(mainMenu);
  }

  /**
   * Show main menu panel
   * @param text Text should be shown on front of planet.
   */
  public void showMainMenu(final String text) {
    mainMenu = new MainMenu(this, text);
    this.updateDisplay(mainMenu);
  }
/**
   * Show Realm View
   * @param realm PlayerInfo whose realm is shown.
   */
  public void showRealmView(final PlayerInfo realm) {
    if (realm == null) {
      realmView = new RealmView(players.getCurrentPlayerInfo(), this, 10, 2);
    } else {
      int knowledge = players.getCurrentPlayerInfo().getKnowledgeBonus(
          players.getIndex(realm));
      DiplomacyBonusList diplomacy = players.getCurrentPlayerInfo()
          .getDiplomacy().getDiplomacyList(players.getIndex(realm));
      int meetings = 0;
      if (diplomacy != null) {
        meetings = diplomacy.getNumberOfMeetings();
      }
      realmView = new RealmView(realm, this, knowledge, meetings);
    }
    this.updateDisplay(realmView);
  }
  /**
   * Show Planet List View
   */
  public void showPlanetListView() {
    planetListView = new PlanetListView(players.getCurrentPlayerInfo(),
        getStarMap(), this);
    this.updateDisplay(planetListView);
  }

  /**
   * Show Galaxy creation panel
   */
  public void showGalaxyCreation() {
    galaxyCreationView = new GalaxyCreationView(galaxyConfig, this);
    galaxyConfig = galaxyCreationView.getConfig();
    this.updateDisplay(galaxyCreationView);
  }

  /**
   * Show Player setup panel
   */
  public void showPlayerSetup() {
    playerSetupView = new PlayerSetupView(galaxyConfig, this);
    this.updateDisplay(playerSetupView);
  }

  /**
   * Show Save Game save panel
   * @param loadedSaveFilename LoadedSaveFilename as a String
   */
  public void showSaveGameSetup(final Object loadedSaveFilename) {
    String filename = "savegame";
    if (playerSetupView != null) {
      filename = playerSetupView.getConfig().getPlayerName(0);
    }
    if (loadedSaveFilename != null && loadedSaveFilename instanceof String) {
      filename = (String) loadedSaveFilename;
    }
    filename = filename.replace(' ', '_');
    saveGameView = new SaveGameNameView(filename, this);
    if (loadedSaveFilename != null && loadedSaveFilename instanceof String) {
      saveGameView.setContinueGame(true);
    }
    this.updateDisplay(saveGameView);
  }

  /**
   * Show Load Game view panel
   */
  public void showLoadGame() {
    loadGameView = new LoadGameView(this);
    this.updateDisplay(loadGameView);
  }


  /**
   * Load game from certain file name
   * @param filename File name
   * @return true if successful
   */
  public boolean loadSavedGame(final String filename) {
    GameRepository repository = new GameRepository();
    starMap = repository.loadGame(GameRepository.DEFAULT_SAVE_FOLDER,
                                  filename);
    return setLoadedGame(starMap);
  }

  /**
   * Show AI Turn view
   */
  public void showAITurnView() {
    aiTurnView = new AITurnView(this);
    this.updateDisplay(aiTurnView);
  }

  /**
   * Show credits panel
   */
  public void showCredits() {
    try {
      creditsView = new CreditsView(this, GAME_TITLE, GAME_VERSION);
    } catch (IOException e) {
      System.out.println("Could not show credits: " + e.getMessage());
      System.exit(0);
    }
    this.updateDisplay(creditsView);
  }

  /**
   * View Leaders view.
   * @param dataObject Planet or fleet where to assign leader.
   */
  public void viewLeaders(final Object dataObject) {
    leaderView = new LeaderView(starMap.getCurrentPlayerInfo(), starMap, this);
    if (dataObject != null) {
      if (dataObject instanceof Planet) {
        Planet planet = (Planet) dataObject;
        leaderView.setPlanet(planet);
      }
      if (dataObject instanceof Fleet) {
        Fleet fleet = (Fleet) dataObject;
        leaderView.setFleet(fleet);
      }
      if (dataObject instanceof Message) {
        Message message = (Message) dataObject;
        if (message.getMatchByString() != null) {
          String[] parts = message.getMatchByString().split(":");
          if (parts.length == 2) {
            try {
              int index = Integer.valueOf(parts[1]);
              leaderView.setFocusToIndex(index);
            } catch (NumberFormatException e) {
              ErrorLogger.log("Leader pool index is not a number! "
                 + e.getMessage());
            }
          }
        }
      }
    }
    this.updateDisplay(leaderView);
    leaderView.updatePanel();
  }
  /**
   * Change game state so that focus is also changed to target message
   * @param newState Game State where to change
   * @param focusMessage Focused message, can be also null
   */
  public void changeGameState(final GameState newState,
      final Message focusMessage) {
    changeGameState(newState, focusMessage, null);
  }

  /**
   * Change game state so that there is custom object given as a parameter
   * @param newState Game State where to change
   * @param dataObject Depends on which state is changed
   */
  public void changeGameState(final GameState newState,
      final Object dataObject) {
    changeGameState(newState, null, dataObject);
  }

  /**
   * Change game state so that focus is also changed to target message.
   * There is also possibility to give data object which depends on new game
   * state.
   * @param newState Game State where to change
   * @param focusMessage Focused message, can be also null
   * @param dataObject Depends on which state is changed
   */
  private void changeGameState(final GameState newState,
      final Message focusMessage, final Object dataObject) {
    if (aiTurnView != null && aiTurnView.isThreaded()) {
      aiTurnView.setNextState(newState, dataObject);
      return;
    }
    if (newState != gameState) {
      previousState = gameState;
      gameState = newState;
    }
    if (animationTimer != null
        && animationTimer.getDelay() != ANIMATION_TIMER_DELAY) {
      animationTimer.setDelay(ANIMATION_TIMER_DELAY);
    }
    switch (gameState) {
    case AITURN:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showAITurnView();
      break;
    case TEXT_SCREEN_VIEW:
      if (dataObject instanceof String) {
        setBridgeCommand(BridgeCommandType.WARM_WHITE);
        showMainMenu((String) dataObject);
      }
      break;
    case MAIN_MENU:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showMainMenu();
      break;
    case GALAXY_CREATION:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showGalaxyCreation();
      break;
    case PLAYER_SETUP:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showPlayerSetup();
      break;
    case SAVE_GAME_NAME_VIEW:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showSaveGameSetup(dataObject);
      break;
    case LOAD_GAME:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showLoadGame();
      break;
    case OPTIONS_VIEW:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showOptionsView();
      break;
    case SETUP_AMBIENT_LIGHTS:
      setBridgeCommand(null);
      showAmbientLightsView();
      break;
    case NEW_GAME: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      makeNewGame(true);
      break;
    }
    case LEADER_VIEW: {
      setBridgeCommand(BridgeCommandType.SPACE_CONSOLE);
      if (focusMessage == null) {
        viewLeaders(dataObject);
      } else {
        viewLeaders(focusMessage);
      }
      break;
    }
    case NEWS_CORP_VIEW: {
      setBridgeCommand(BridgeCommandType.FADE_IN_START);
      showNewsCorpView();
      break;
    }
    case ESPIONAGE_VIEW: {
      setBridgeCommand(BridgeCommandType.SPACE_CONSOLE2);
      showEspionageView();
      break;
    }
    case HELP_VIEW: {
      setBridgeCommand(BridgeCommandType.GREEN_CONSOLE);
      showHelp();
      break;
    }
    case HISTORY_VIEW: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showHistoryView();
      break;
    }
    case PLANETBOMBINGVIEW: {
      setBridgeCommand(BridgeCommandType.YELLOW_ALERT);
      planetBombingView(dataObject);
      break;
    }
    case CREDITS:
      setBridgeCommand(BridgeCommandType.DARKEST);
      if (animationTimer != null) {
        animationTimer.setDelay(ANIMATION_DELAY_CREDITS);
      }
      showCredits();
      break;
    case STARMAP:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      if (animationTimer != null) {
        animationTimer.setDelay(30);
      }
      MusicPlayer.activeFadeout();
      showStarMap(dataObject);
      break;
    case COMBAT: {
      setBridgeCommand(BridgeCommandType.RED_ALERT);
      if (animationTimer != null) {
        animationTimer.setDelay(ANIMATION_DELAY_COMBAT);
      }
      combat(dataObject);
      break;
    }
    case RESEARCHVIEW:
      setBridgeCommand(BridgeCommandType.GREEN_CONSOLE);
      showResearch(focusMessage);
      break;
    case VIEWSHIPS:
      setBridgeCommand(BridgeCommandType.SPACE_CONSOLE);
      showShipView();
      break;
    case VIEWSTATS:
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showStatView();
      break;
    case VOTE_VIEW:
      setBridgeCommand(BridgeCommandType.SPACE_CONSOLE3);
      showVoteView();
      break;
    case SHIPDESIGN: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      shipDesign();
      break;
    }
    case PLANETVIEW: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      if (dataObject == null) {
        planetView(focusMessage);
      } else if (dataObject instanceof Planet) {
        Planet planet = (Planet) dataObject;
        showPlanetView(planet, planet.getPlanetPlayerInfo(), true);
      }
      break;
    }
    case ESPIONAGE_MISSIONS_VIEW: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      if (dataObject instanceof Planet) {
        Planet planet = (Planet) dataObject;
        showEspionageMissionView(planet, getPlayers().getCurrentPlayerInfo());
      }
      break;
    }
    case FLEETVIEW: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      fleetView();
      break;
    }
    case SHIP_UPGRADE_VIEW: {
      setBridgeCommand(BridgeCommandType.SPACE_CONSOLE3);
      showFleetUpgradeView();
      break;
    }
    case FLEET_TRADE_VIEW: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      fleetTradeView();
      break;
    }
    case DIPLOMACY_VIEW: {
      showDiplomacyView(dataObject);
      break;
    }
    case REALM_VIEW: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      if (dataObject instanceof PlayerInfo) {
        showRealmView((PlayerInfo) dataObject);
      }
      break;
    }
    case PLANET_LIST_VIEW: {
      setBridgeCommand(BridgeCommandType.FLOAT_IN_SPACE);
      showPlanetListView();
      break;
    }
    default: {
        showMainMenu();
    }
    }
  }

  /**
   * Show fleet View
   */
  private void fleetView() {
    if (starMapView.getStarMapMouseListener()
             .getLastClickedFleet() != null) {
        Fleet fleet = starMapView.getStarMapMouseListener()
                .getLastClickedFleet();
        Planet planet = starMap.getPlanetByCoordinate(fleet.getX(),
            fleet.getY());
        boolean interactive = false;
        if (starMap.getCurrentPlayerInfo()
                == starMap.getPlayerInfoByFleet(fleet)) {
          interactive = true;
        }
        showFleetView(planet, fleet, interactive);
      }
  }

  /**
   * Show fleet Trade View
   */
  private void fleetTradeView() {
    if (starMapView.getStarMapMouseListener()
             .getLastClickedFleet() != null) {
        Fleet fleet = starMapView.getStarMapMouseListener()
                .getLastClickedFleet();
        Planet planet = starMap.getPlanetNextToCoordinate(
            fleet.getCoordinate());
        if (starMap.getCurrentPlayerInfo()
                == starMap.getPlayerInfoByFleet(fleet) && planet != null) {
          showFleetTradeView(planet, fleet);
        }
      }
  }


  /**
   * Show planet View
   * @param focusMessage Focused message, can be also null
   */
  private void planetView(final Message focusMessage) {
   if (focusMessage != null) {
        Planet planet = starMap.getPlanetByCoordinate(focusMessage.getX(),
            focusMessage.getY());
        if (planet != null) {
          boolean interactive = false;
          if (starMap.getCurrentPlayerInfo() == planet.getPlanetPlayerInfo()) {
            interactive = true;
          }
          starMap.setCursorPos(focusMessage.getX(), focusMessage.getY());
          starMap.setDrawPos(focusMessage.getX(), focusMessage.getY());
          showPlanetView(planet, starMap.getCurrentPlayerInfo(), interactive);
        }
      } else if (starMapView.getStarMapMouseListener()
              .getLastClickedPlanet() != null) {
        boolean interactive = false;
        Planet planet = starMapView.getStarMapMouseListener()
            .getLastClickedPlanet();
        if (starMap.getCurrentPlayerInfo() == planet.getPlanetPlayerInfo()) {
          interactive = true;
        }
        showPlanetView(planet, starMap.getCurrentPlayerInfo(), interactive);
      }
  }


  /**
   * Show ship design
   */
  private void shipDesign() {
   if (shipView != null && shipView.isCopyClicked()) {
        showShipDesignView(shipView.getSelectedShip());
      } else {
        showShipDesignView(null);
      }

  }


  /**
   * Show combat
   * @param dataObject Depends on which state is changed
   */
  private void combat(final Object dataObject) {
    if (dataObject instanceof Combat) {
        showCombat((Combat) dataObject);
      } else {
        showCombat(null);
      }

  }


  /**
   * Show planetBombingView
   * @param dataObject Depends on which state is changed
   */
  private void planetBombingView(final Object dataObject) {
    boolean changed = false;
    if (dataObject instanceof FleetView) {
      FleetView view = (FleetView) dataObject;
      Planet planet = view.getPlanet();
      Fleet fleet = view.getFleet();
      if (fleet != null && planet != null
          && fleet.getX() == planet.getX()
          && fleet.getY() == planet.getY()) {
        showPlanetBombingView(planet, fleet);
        changed = true;
      }
    }
    if (dataObject instanceof PlanetBombingView) {
      showPlanetBombingView((PlanetBombingView) dataObject);
      changed = true;
    }
    if (!changed) {
      changeGameState(GameState.STARMAP);
    }

  }


  /**
   * Make new Game State
   * @param allowHumanAncientRealm Flag for allowing human player to be
   *  ancient too.
   */
  public void makeNewGame(final boolean allowHumanAncientRealm) {
    setPlayerInfo();
    starMap = new StarMap(galaxyConfig, players);
    starMap.updateStarMapOnStartGame();
    NewsCorpData corpData = starMap.getNewsCorpData();
    calculateCorpData(corpData);
    boolean ancientRealmStart = false;
    for (int i = 0; i < galaxyConfig.getMaxPlayers(); i++) {
      if (galaxyConfig.getPlayerAncientRealm(i)) {
        ancientRealmStart = true;
      }
    }
    if (ancientRealmStart) {
      if (allowHumanAncientRealm) {
        starMap.getPlayerByIndex(0).setHuman(false);
      }
      starMap.setTurn(-galaxyConfig.getAncientHeadStart());
      while (starMap.getTurn() < 0) {
        setAITurnView(new AITurnView(this));
        boolean singleTurnEnd = false;
        do {
          singleTurnEnd = getAITurnView().handleAiTurn();
        } while (!singleTurnEnd);
      }
      if (allowHumanAncientRealm && !galaxyConfig.isAiOnly()) {
        starMap.getPlayerByIndex(0).setHuman(true);
        starMap.getPlayerByIndex(0).getMissions().clearMissions();
        PlayerInfo info = starMap.getPlayerByIndex(0);
        info.getTechList().setTechFocus(TechType.Combat, 20);
        info.getTechList().setTechFocus(TechType.Defense, 16);
        info.getTechList().setTechFocus(TechType.Hulls, 16);
        info.getTechList().setTechFocus(TechType.Improvements, 16);
        info.getTechList().setTechFocus(TechType.Electrics, 16);
        info.getTechList().setTechFocus(TechType.Propulsion, 16);
      }
      for (Planet planet : starMap.getPlanetList()) {
        if (planet.getstartRealmIndex() != -1) {
          int index = planet.getstartRealmIndex();
          PlayerInfo info = starMap.getPlayerByIndex(index);
          if (!info.isAncientRealm()) {
            starMap.createRealmToPlanet(planet, info, index);
          }
        }
      }
      starMap.clearNewsCorpData();
      corpData = starMap.getNewsCorpData();
      calculateCorpData(corpData);
      starMap.updateStarMapOnStartGame();
    }
    players.setCurrentPlayer(0);
    setNullView();
    changeGameState(GameState.STARMAP);

  }


  /**
   * Calculate Corporation Data
   * @param corpData various calculations
   */
  private void calculateCorpData(final NewsCorpData corpData) {
    corpData.calculateCredit(players);
    corpData.calculateCulture(starMap.getPlanetList(), players);
    corpData.calculateMilitary(players, false);
    corpData.calculatePlanets(starMap.getPlanetList());
    corpData.calculatePopulation(starMap.getPlanetList());
    corpData.calculateResearch(players);
  }


  /**
   * Set Player information when make new game
   */
  public void setPlayerInfo() {
    players = new PlayerList();
    int boardIndex = -1;
    int maxPlayers = galaxyConfig.getMaxPlayers();
    if (galaxyConfig.getSpacePiratesLevel() > 0) {
      maxPlayers++;
      boardIndex = galaxyConfig.getMaxPlayers();
    }
    ArrayList<PlayerColor> randomListOfColors = new ArrayList<>();
    for (PlayerColor color : PlayerColor.values()) {
      randomListOfColors.add(color);
    }
    for (int i = 0; i < galaxyConfig.getMaxPlayers(); i++) {
      PlayerInfo info = new PlayerInfo(galaxyConfig.getRace(i),
          maxPlayers, i, boardIndex);
      info.setGovernment(galaxyConfig.getPlayerGovernment(i));
      info.setEmpireName(galaxyConfig.getPlayerName(i));
      info.setAncientRealm(galaxyConfig.getPlayerAncientRealm(i));
      info.setAiDifficulty(galaxyConfig.getDifficulty(i));
      info.setColor(galaxyConfig.getPlayerColor(i));
      randomListOfColors.remove(galaxyConfig.getPlayerColor(i));
      if (i == 0 && !galaxyConfig.isAiOnly()) {
        info.setHuman(true);
      }
      if (info.isHuman()) {
        info.setAiDifficulty(AiDifficulty.CHALLENGING);
      }
      players.addPlayer(info);
    }
    if (galaxyConfig.getSpacePiratesLevel() > 0) {
      int index = galaxyConfig.getMaxPlayers();
      PlayerInfo info = new PlayerInfo(SpaceRace.SPACE_PIRATE, maxPlayers,
          index, boardIndex);
      info.setBoard(true);
      info.setGovernment(GovernmentType.SPACE_PIRATES);
      info.setEmpireName("Space pirates");
      PirateDifficultLevel difficultyLevel = galaxyConfig
          .getSpacePiratesDifficulty();
      if (difficultyLevel == PirateDifficultLevel.EASY
          || difficultyLevel == PirateDifficultLevel.VERY_EASY) {
        info.setAiDifficulty(AiDifficulty.WEAK);
      }
      if (difficultyLevel == PirateDifficultLevel.NORMAL) {
        info.setAiDifficulty(AiDifficulty.NORMAL);
      }
      if (difficultyLevel == PirateDifficultLevel.HARD
          || difficultyLevel == PirateDifficultLevel.VERY_HARD) {
        info.setAiDifficulty(AiDifficulty.CHALLENGING);
      }
      int colorIndex = DiceGenerator.getRandom(randomListOfColors.size() - 1);
      info.setColor(randomListOfColors.get(colorIndex));
      players.addPlayer(info);
    }
    players.calculateInitialDiplomacyBonuses();
  }


  /**
   * Set null in view when make new game
   */
  private void setNullView() {
      starMapView = null;
      combatView = null;
      researchView = null;
      shipView = null;
      shipDesignView = null;

  }

  /**
   * Change game state and show new panel/screen
   * @param newState Game State where to change
   */
  public void changeGameState(final GameState newState) {
    changeGameState(newState, null);
  }

  /**
   * Print Tech into a markdown format
   * @param techNames Technames to print
   * @param type Tech type
   * @param level Which tech level
   * @return String of tech info in markdown format
   */
  public static String printTech(final String[] techNames,
      final TechType type, final int level) {
    StringBuilder sb = new StringBuilder();
    sb.append("\n### " + type.toString() + " - " + level + "\n\n");
    for (int i = 0; i < techNames.length; i++) {
      Tech tech = TechFactory.createTech(type, level, techNames[i]);
      boolean noPrint = true;
      if (tech.getComponent() != null) {
        ShipComponent comp = ShipComponentFactory.createByName(
            tech.getComponent());
        sb.append(i + 1);
        sb.append(". ");
        sb.append(comp.toString());
        sb.append("\n\n");
        noPrint = false;
      }
      if (tech.getImprovement() != null) {
        Building build = BuildingFactory.createByName(tech.getImprovement());
        sb.append(i + 1);
        sb.append(". ");
        sb.append(build.getFullDescription());
        sb.append("\n\n");
        noPrint = false;
      }
      if (tech.getHull() != null) {
        ShipHull hull = ShipHullFactory.createByName(tech.getHull(),
            SpaceRace.HUMAN);
        sb.append(i + 1);
        sb.append(". ");
        sb.append(hull.toString());
        sb.append("\n\n");
        noPrint = false;
      }
      if (noPrint) {
        if (tech.getName().equals("Deadly virus")) {
          sb.append(i + 1);
          sb.append(". ");
          sb.append(" Allows deadly virus espionage mission.\n");
        } else if (tech.getName().contains("colonization")) {
          sb.append(i + 1);
          sb.append(". ");
          sb.append(" Improves planet suitability.\n");
        } else if (tech.getName().equals("Improved engineer")) {
          sb.append(i + 1);
          sb.append(". ");
          sb.append(" Reduces overload failure by 2.\n");
        } else {
          throw new IllegalArgumentException("Tech with not actually used: "
             + tech.getName());
        }
      }
    }
    return sb.toString();
  }
  /**
   * Print whole research wiki page contain all information about tech
   * @return Research wiki page as a String
   */
  public static String printTechWiki() {
    StringBuilder sb = new StringBuilder();
    sb.append("## Combat technology\n\n");
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL1_NAMES,
        TechType.Combat, 1));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL2_NAMES,
        TechType.Combat, 2));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL3_NAMES,
        TechType.Combat, 3));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL4_NAMES,
        TechType.Combat, 4));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL5_NAMES,
        TechType.Combat, 5));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL6_NAMES,
        TechType.Combat, 6));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL7_NAMES,
        TechType.Combat, 7));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL8_NAMES,
        TechType.Combat, 8));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL9_NAMES,
        TechType.Combat, 9));
    sb.append(printTech(TechFactory.COMBAT_TECH_LEVEL10_NAMES,
        TechType.Combat, 10));
    sb.append("## Combat rare technology\n\n");
    sb.append(printTech(TechFactory.COMBAT_RARE_TECH_LEVEL2_NAMES,
        TechType.Combat, 2));
    sb.append(printTech(TechFactory.COMBAT_RARE_TECH_LEVEL3_NAMES,
        TechType.Combat, 3));
    sb.append(printTech(TechFactory.COMBAT_RARE_TECH_LEVEL5_NAMES,
        TechType.Combat, 5));
    sb.append(printTech(TechFactory.COMBAT_RARE_TECH_LEVEL7_NAMES,
        TechType.Combat, 7));
    sb.append(printTech(TechFactory.COMBAT_RARE_TECH_LEVEL9_NAMES,
        TechType.Combat, 9));
    sb.append("## Defense technology\n\n");
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL1_NAMES,
        TechType.Defense, 1));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL2_NAMES,
        TechType.Defense, 2));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL3_NAMES,
        TechType.Defense, 3));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL4_NAMES,
        TechType.Defense, 4));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL5_NAMES,
        TechType.Defense, 5));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL6_NAMES,
        TechType.Defense, 6));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL7_NAMES,
        TechType.Defense, 7));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL8_NAMES,
        TechType.Defense, 8));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL9_NAMES,
        TechType.Defense, 9));
    sb.append(printTech(TechFactory.DEFENSE_TECH_LEVEL10_NAMES,
        TechType.Defense, 10));
    sb.append("## Defense rare technology\n\n");
    sb.append(printTech(TechFactory.DEFENSE_RARE_TECH_LEVEL3_NAMES,
        TechType.Defense, 3));
    sb.append(printTech(TechFactory.DEFENSE_RARE_TECH_LEVEL4_NAMES,
        TechType.Defense, 4));
    sb.append(printTech(TechFactory.DEFENSE_RARE_TECH_LEVEL6_NAMES,
        TechType.Defense, 6));
    sb.append(printTech(TechFactory.DEFENSE_RARE_TECH_LEVEL8_NAMES,
        TechType.Defense, 8));
    sb.append(printTech(TechFactory.DEFENSE_RARE_TECH_LEVEL9_NAMES,
        TechType.Defense, 9));
    sb.append("## Hull technology\n\n");
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL1_NAMES,
        TechType.Hulls, 1));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL2_NAMES,
        TechType.Hulls, 2));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL3_NAMES,
        TechType.Hulls, 3));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL4_NAMES,
        TechType.Hulls, 4));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL5_NAMES,
        TechType.Hulls, 5));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL6_NAMES,
        TechType.Hulls, 6));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL7_NAMES,
        TechType.Hulls, 7));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL8_NAMES,
        TechType.Hulls, 8));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL9_NAMES,
        TechType.Hulls, 9));
    sb.append(printTech(TechFactory.HULL_TECH_LEVEL10_NAMES,
        TechType.Hulls, 10));
    sb.append("## Planetary Improvement technology\n\n");
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL1_NAMES,
        TechType.Improvements, 1));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL2_NAMES,
        TechType.Improvements, 2));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL3_NAMES,
        TechType.Improvements, 3));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL4_NAMES,
        TechType.Improvements, 4));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL5_NAMES,
        TechType.Improvements, 5));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL6_NAMES,
        TechType.Improvements, 6));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL7_NAMES,
        TechType.Improvements, 7));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL8_NAMES,
        TechType.Improvements, 8));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL9_NAMES,
        TechType.Improvements, 9));
    sb.append(printTech(TechFactory.IMPROVEMENT_TECH_LEVEL10_NAMES,
        TechType.Improvements, 10));
    sb.append("## Planetary Improvement rare technology\n\n");
    sb.append(printTech(TechFactory.IMPROVEMENT_RARE_TECH_LEVEL4_NAMES,
        TechType.Improvements, 4));
    sb.append("## Propulsion technology\n\n");
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL1_NAMES,
        TechType.Propulsion, 1));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL2_NAMES,
        TechType.Propulsion, 2));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL3_NAMES,
        TechType.Propulsion, 3));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL4_NAMES,
        TechType.Propulsion, 4));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL5_NAMES,
        TechType.Propulsion, 5));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL6_NAMES,
        TechType.Propulsion, 6));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL7_NAMES,
        TechType.Propulsion, 7));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL8_NAMES,
        TechType.Propulsion, 8));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL9_NAMES,
        TechType.Propulsion, 9));
    sb.append(printTech(TechFactory.PROPULSION_TECH_LEVEL10_NAMES,
        TechType.Propulsion, 10));
    sb.append("## Electronics technology\n\n");
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL1_NAMES,
        TechType.Electrics, 1));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL2_NAMES,
        TechType.Electrics, 2));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL3_NAMES,
        TechType.Electrics, 3));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL4_NAMES,
        TechType.Electrics, 4));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL5_NAMES,
        TechType.Electrics, 5));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL6_NAMES,
        TechType.Electrics, 6));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL7_NAMES,
        TechType.Electrics, 7));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL8_NAMES,
        TechType.Electrics, 8));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL9_NAMES,
        TechType.Electrics, 9));
    sb.append(printTech(TechFactory.ELECTRONICS_TECH_LEVEL10_NAMES,
        TechType.Electrics, 10));
    return sb.toString();
  }

  /**
   * Update all save files in default save folder.
   * This is dev tool, which is use to upgrade previous
   * save format to new one.
   */
  public static void saveGameUpdate() {
    try {
      readTutorial(null);
    } catch (IOException e1) {
      ErrorLogger.log(e1);
    }
    File folder = new File("saves");
    File[] files = folder.listFiles(new GenericFileFilter(".save"));
    if (files == null) {
      files = new File[0];
    }
    GameRepository repository = new GameRepository();
    for (int i = 0; i < files.length; i++) {
      Game game = new Game(false);
      if (!game.loadSavedGame(files[i].getName())) {
        System.err.println("Failed upgrade save file: "
            + files[i].getName());
      } else {
        repository.saveGame(GameRepository.DEFAULT_SAVE_FOLDER,
            files[i].getName(), game.getStarMap());
        System.out.println(files[i].getName() + " saved!");
      }
    }
  }

  /**
   * Main method to run the game
   * @param args from Command line
   */
  public static void main(final String[] args) {
    MusicPlayer.setMusicEnabled(true);
    SoundPlayer.setSoundEnabled(true);
    if (args.length > 0 && args[0].equals("--credits")) {
      System.out.println("# Authors of Open Realm Of Stars\n");
      System.out.println(CreditsView.MAIN_CREDITS);
    } else if (args.length > 0 && args[0].equals("--wiki-research")) {
      System.out.println(printTechWiki());
    } else if (args.length > 0 && args[0].equals("--save-update")) {
      saveGameUpdate();
    } else if (args.length > 1 && args[0].equals("--text")) {
      System.out.println("Disabling the music...");
      MusicPlayer.setMusicEnabled(false);
      Game game = new Game(true);
      game.changeGameState(GameState.TEXT_SCREEN_VIEW, args[1]);
    } else {
      if (args.length > 0 && args[0].equals("--no-music")) {
        System.out.println("Disabling the music...");
        MusicPlayer.setMusicEnabled(false);
      }
      if (args.length > 0 && args[0].equals("--debug")) {
        System.out.println("Debugging enabled.");
        ErrorLogger.enabledDebugging();
      }
      mainMethodCalled = true;
      new Game(true);
    }

  }

  /**
   * Has main method called or not.
   * @return True if game is actually being run. False for example JUnits.
   */
  public static boolean isMainMethodCalled() {
    return mainMethodCalled;
  }
  /**
   * Focus on active message
   * @param mapOnly focus only message which move map
   */
  public void focusOnMessage(final boolean mapOnly) {
    Message msg = players.getCurrentPlayerInfo().getMsgList().getMsg();
    if (msg.getType() == MessageType.RESEARCH && !mapOnly) {
      changeGameState(GameState.RESEARCHVIEW, msg);
    }
    if (msg.getType() == MessageType.NEWS && !mapOnly) {
      changeGameState(GameState.NEWS_CORP_VIEW);
    }
    if (msg.getType() == MessageType.PLANETARY) {
      starMap.setCursorPos(msg.getX(), msg.getY());
      starMap.setDrawPos(msg.getX(), msg.getY());
      Planet planet = starMap.getPlanetByCoordinate(msg.getX(), msg.getY());
      if (planet != null) {
        starMapView.setShowPlanet(planet);
        starMapView.setCursorFocus(20);
        starMapView.getStarMapMouseListener().setLastClickedPlanet(planet);
      }
    }
    if (msg.getType() == MessageType.FLEET) {
      starMap.setCursorPos(msg.getX(), msg.getY());
      starMap.setDrawPos(msg.getX(), msg.getY());
      Fleet fleet = players.getCurrentPlayerInfo().getFleets()
          .getByName(msg.getMatchByString());
      if (fleet != null) {
        starMapView.setShowFleet(fleet);
        starMapView.setCursorFocus(20);
        starMapView.getStarMapMouseListener().setLastClickedFleet(fleet);
        starMapView.getStarMapMouseListener().setLastClickedPlanet(null);
      }
    }
    if ((msg.getType() == MessageType.FLEET
        || msg.getType() == MessageType.PLANETARY
        || msg.getType() == MessageType.INFORMATION)
        && players.getCurrentPlayerInfo().getRandomEventOccured() != null
        && !players.getCurrentPlayerInfo().getRandomEventOccured()
            .isPopupShown()
        && msg.isRandomEventPop()) {
      PopupPanel popupPanel = new PopupPanel(
          players.getCurrentPlayerInfo().getRandomEventOccured());
      starMapView.setPopup(popupPanel);
      players.getCurrentPlayerInfo().getRandomEventOccured().setPopupShown(
          true);
      msg.setRandomEventPop(false);
    }
    if (mapOnly) {
      starMapView.setCursorFocus(20);
    }
    if ((msg.getType() == MessageType.CONSTRUCTION
        || msg.getType() == MessageType.POPULATION) && !mapOnly) {
      changeGameState(GameState.PLANETVIEW, msg);
    }
    if (msg.getType() == MessageType.LEADER && !mapOnly) {
      changeGameState(GameState.LEADER_VIEW, msg);
    }
  }


  /**
   * Colonize Planet Action. This should be called when player colonized a
   * planet.
   */
  private void colonizePlanetAction() {
    Ship ship = fleetView.getFleet().getColonyShip();
    if (ship != null && fleetView.getPlanet() != null
        && fleetView.getPlanet().getPlanetPlayerInfo() == null) {
      // Make sure that ship is really colony and there is planet to
      // colonize
      fleetView.getPlanet().setPlanetOwner(players.getCurrentPlayer(),
          players.getCurrentPlayerInfo());
      if (players.getCurrentPlayerInfo().getRace() == SpaceRace.ALTEIRIANS) {
        fleetView.getPlanet().colonizeWithOrbital();
      }
      if (players.getCurrentPlayerInfo().getRace() == SpaceRace.MECHIONS) {
        fleetView.getPlanet().setWorkers(Planet.PRODUCTION_WORKERS,
            ship.getColonist());
      } else if (players.getCurrentPlayerInfo().getRace().isLithovorian()) {
        fleetView.getPlanet().setWorkers(Planet.METAL_MINERS,
            ship.getColonist());
      } else {
        fleetView.getPlanet().setWorkers(Planet.FOOD_FARMERS,
            ship.getColonist());
      }
      // Remove the ship and show the planet view you just colonized
      fleetView.getFleet().removeShip(ship);
      ShipStat stat = starMap.getCurrentPlayerInfo()
          .getShipStatByName(ship.getName());
      if (stat != null) {
        stat.setNumberOfInUse(stat.getNumberOfInUse() - 1);
      }
      if (fleetView.getFleet().getNumberOfShip() == 0
          && fleetView.getFleet().getCommander() != null) {
        fleetView.getFleet().getCommander().setJob(Job.GOVERNOR);
        fleetView.getPlanet().setGovernor(fleetView.getFleet().getCommander());
        fleetView.getPlanet().getGovernor().setTitle(
            LeaderUtility.createTitleForLeader(
                fleetView.getPlanet().getGovernor(),
                players.getCurrentPlayerInfo()));
        fleetView.getFleet().setCommander(null);
      }
      fleetView.getFleetList().recalculateList();
      starMapView.getStarMapMouseListener().setLastClickedFleet(null);
      starMapView.getStarMapMouseListener()
          .setLastClickedPlanet(fleetView.getPlanet());
      EventOnPlanet event = new EventOnPlanet(EventType.PLANET_COLONIZED,
          fleetView.getPlanet().getCoordinate(),
          fleetView.getPlanet().getName(), players.getCurrentPlayer());
      event.setText(players.getCurrentPlayerInfo().getEmpireName()
          + " colonized planet " + fleetView.getPlanet().getName()
          + ". ");
      SoundPlayer.playSound(SoundPlayer.COLONIZED);
      starMap.getHistory().addEvent(event);
      fleetView.getPlanet().eventActivation();
      changeGameState(GameState.PLANETVIEW);
    }
  }

  /**
   * Change message focus for fleet
   * @param fleet Where to focus
   */
  private void changeMessageForFleets(final Fleet fleet) {
    changeMessage = new ChangeMessageFleet(starMap, starMapView);
    changeMessage.changeMessage(fleet);
  }

  /**
   * Change message focus for Planet
   * @param planet Where to focus
   */
  private void changeMessageForPlanet(final Planet planet) {
    changeMessage = new ChangeMessagePlanet(starMap, starMapView);
    changeMessage.changeMessage(planet);
  }

  /**
   * Create conflict ship image with possible planet background.
   * @param cloaked If ship is cloaked or not.
   * @param planet Possible planet background
   * @return ImageInstructions
   */
  public static ImageInstruction createConflictingShipImage(
      final boolean cloaked, final Planet planet) {
    ImageInstruction instructions = new ImageInstruction();
    if (DiceGenerator.getRandom(1) == 0) {
      instructions.addBackground(ImageInstruction.BACKGROUND_STARS);
    } else {
      instructions.addBackground(ImageInstruction.BACKGROUND_NEBULAE);
    }
    String position = ImageInstruction.POSITION_CENTER;
    switch (DiceGenerator.getRandom(3)) {
      case 0: {
        position = ImageInstruction.POSITION_CENTER;
        break;
      }
      case 1: {
        position = ImageInstruction.POSITION_LEFT;
        break;
      }
      case 2:
      default: {
        position = ImageInstruction.POSITION_RIGHT;
        break;
      }
    }
    String size = ImageInstruction.SIZE_FULL;
    switch (DiceGenerator.getRandom(2)) {
      case 0: {
        size = ImageInstruction.SIZE_FULL;
        break;
      }
      case 1:
      default: {
        size = ImageInstruction.SIZE_HALF;
        break;
      }
    }
    if (planet != null) {
      instructions.addPlanet(position, planet.getImageInstructions(), size);
    }
    position = ImageInstruction.POSITION_CENTER;
    switch (DiceGenerator.getRandom(3)) {
      case 0: {
        position = ImageInstruction.POSITION_CENTER;
        break;
      }
      case 1: {
        position = ImageInstruction.POSITION_LEFT;
        break;
      }
      case 2:
      default: {
        position = ImageInstruction.POSITION_RIGHT;
        break;
      }
    }
    size = ImageInstruction.SIZE_FULL;
    switch (DiceGenerator.getRandom(2)) {
      case 0: {
        size = ImageInstruction.SIZE_FULL;
        break;
      }
      case 1:
      default: {
        size = ImageInstruction.SIZE_HALF;
        break;
      }
    }
    String ship = ImageInstruction.TRADER1;
    if (cloaked) {
      ship = ImageInstruction.CLOAKED_SHIP;
    } else {
      switch (DiceGenerator.getRandom(3)) {
        case 0: {
          ship = ImageInstruction.TRADER1;
          break;
        }
        case 1:
        default: {
          ship = ImageInstruction.TRADER2;
          break;
        }
        case 2: {
          ship = ImageInstruction.SHUTTLE2;
          break;
        }
      }
    }
    instructions.addTrader(position, ship, size);
    return instructions;
  }
  /**
   * Handle state changes via double clicking on StarMap
   */
  private void handleDoubleClicksOnStarMap() {
    // Handle double click state changes
    if (starMapView.getStarMapMouseListener().isDoubleClicked()) {
      if (starMapView.getStarMapMouseListener()
          .getLastClickedPlanet() != null) {
        changeGameState(GameState.PLANETVIEW);
      } else if (starMapView.getStarMapMouseListener()
          .getLastClickedFleet() != null) {
        changeGameState(GameState.FLEETVIEW);
      }
    }
    if (!starMapView.getStarMapMouseListener().isDoubleClicked()
        && starMapView.getStarMapMouseListener().isMoveClicked()
        && starMapView.getStarMapMouseListener()
            .getLastClickedFleet() != null) {
      Fleet fleet = starMapView.getStarMapMouseListener().getLastClickedFleet();
      if (fleet.getRoute() != null && fleet.getRoute().isBombing()) {
        starMapView.getStarMapMouseListener().setMoveClicked(false);
        return;
      }
      if (starMapView.checkForConflict()) {
        return;
      }
      fleet.setRoute(null);
      starMapView.getStarMapMouseListener().setWarningShown(false);
      starMapView.getStarMapMouseListener().setMoveClicked(false);
      fleetMakeMove(players.getCurrentPlayerInfo(),
          starMapView.getStarMapMouseListener().getLastClickedFleet(),
          starMapView.getStarMapMouseListener().getMoveX(),
          starMapView.getStarMapMouseListener().getMoveY());
    }
  }

  /**
   * Actions performed when state is menus and not exactly in game
   * @param arg0 ActionEvent which has occured
   */
  private void actionPerformedMenus(final ActionEvent arg0) {
    if (gameState == GameState.CREDITS) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_ANIMATION_TIMER)) {
        creditsView.updateTextArea();
        return;
      }
      if (arg0.getActionCommand().equalsIgnoreCase(GameCommands.COMMAND_OK)) {
        creditsView = null;
        SoundPlayer.playMenuSound();
        changeGameState(GameState.MAIN_MENU);
      }
      return;
    }
    if (gameState == GameState.GALAXY_CREATION && galaxyCreationView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_CANCEL)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.MAIN_MENU);
        return;
      } else if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_NEXT)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.PLAYER_SETUP);
        return;
      } else {
        galaxyCreationView.handleActions(arg0);
        return;
      }
    } else if (gameState == GameState.PLAYER_SETUP && playerSetupView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_CANCEL)) {
        SoundPlayer.playMenuSound();
        playerSetupView.getNamesToConfig();
        changeGameState(GameState.GALAXY_CREATION);
        return;
      } else if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_NEXT)) {
        SoundPlayer.playMenuSound();
        playerSetupView.getNamesToConfig();
        changeGameState(GameState.SAVE_GAME_NAME_VIEW);
        return;
      } else {
        playerSetupView.handleActions(arg0);
        return;
      }
    } else if (gameState == GameState.SAVE_GAME_NAME_VIEW
        && saveGameView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_CANCEL)) {
        SoundPlayer.playMenuSound();
        if (!saveGameView.isContinueGame()) {
          changeGameState(GameState.PLAYER_SETUP);
        } else {
          changeGameState(GameState.LOAD_GAME);
        }
        return;
      } else if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_NEXT)) {
        SoundPlayer.playMenuSound();
        if (!saveGameView.isContinueGame()) {
          playerSetupView.getNamesToConfig();
          saveFilename = saveGameView.getFilename();
          changeGameState(GameState.NEW_GAME);
        } else {
          saveFilename = saveGameView.getFilename();
          changeGameState(GameState.STARMAP);
        }
        return;
      } else {
        saveGameView.handleActions(arg0);
        return;
      }
    }
    if (gameState == GameState.LOAD_GAME && loadGameView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_CANCEL)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.MAIN_MENU);
        return;
      } else if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_NEXT)
          && loadGameView.getSelectedSaveFile() != null
          && loadSavedGame(loadGameView.getSelectedSaveFile())) {
        saveFilename = loadGameView.getSelectedSaveFile();
        SoundPlayer.playMenuSound();
        if (saveFilename.equals("autosave.save")) {
          saveFilename = starMap.getPlayerByIndex(0).getEmpireName();
          changeGameState(GameState.SAVE_GAME_NAME_VIEW, saveFilename);
        } else {
          changeGameState(GameState.STARMAP);
        }
        return;
      }
    }
    if (gameState == GameState.OPTIONS_VIEW && optionsView != null) {
      // Options
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_OK)) {
        SoundPlayer.playMenuSound();
        if (!optionsView.getResolution().equals(getCurrentResolution())
            && !isFullscreenMode()) {
          setNewResolution(optionsView.getResolution());
        }
        if (gameFrame.isResizable() && !isFullscreenMode()) {
          setResizable(false);
          setNewResolution(gameFrame.getWidth() + "x" + gameFrame.getHeight());
        }
        configFile.setMusicVolume(optionsView.getMusicVolume());
        configFile.setSoundVolume(optionsView.getSoundVolume());
        configFile.setBorderless(optionsView.getBorderless());
        configFile.setHardwareAcceleration(
            optionsView.getHardwareAcceleration());
        configFile.setFullscreen(optionsView.getFullscreen());
        configFile.setLargerFonts(optionsView.getLargerFonts());
        configFile.setResolution(gameFrame.getWidth(), gameFrame.getHeight());
        GuiStatics.setLargerFonts(configFile.getLargerFonts());
        configFile.setLightIntense(optionsView.getIntense());
        configFile.setAmbientLights(optionsView.isLightsEnabled());
        setBridgeCommand(BridgeCommandType.EXIT);
        writeConfigFile();
        initBridge();
        changeGameState(GameState.MAIN_MENU);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_SETUP_LIGHTS)) {
        SoundPlayer.playMenuSound();
        if (!optionsView.getResolution().equals(getCurrentResolution())
            && !isFullscreenMode()) {
          setNewResolution(optionsView.getResolution());
        }
        if (gameFrame.isResizable()
            && !isFullscreenMode()) {
          setResizable(false);
          setNewResolution(gameFrame.getWidth() + "x" + gameFrame.getHeight());
        }
        configFile.setMusicVolume(optionsView.getMusicVolume());
        configFile.setSoundVolume(optionsView.getSoundVolume());
        configFile.setBorderless(optionsView.getBorderless());
        configFile.setHardwareAcceleration(
            optionsView.getHardwareAcceleration());
        configFile.setFullscreen(optionsView.getFullscreen());
        configFile.setLargerFonts(optionsView.getLargerFonts());
        configFile.setResolution(gameFrame.getWidth(), gameFrame.getHeight());
        GuiStatics.setLargerFonts(configFile.getLargerFonts());
        configFile.setLightIntense(optionsView.getIntense());
        configFile.setAmbientLights(optionsView.isLightsEnabled());
        writeConfigFile();
        changeGameState(GameState.SETUP_AMBIENT_LIGHTS);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_APPLY)) {
        SoundPlayer.playMenuSound();
        if (!optionsView.getResolution().equals("Custom")
            && !isFullscreenMode()) {
          setNewResolution(optionsView.getResolution());
        }
        configFile.setMusicVolume(optionsView.getMusicVolume());
        configFile.setSoundVolume(optionsView.getSoundVolume());
        configFile.setBorderless(optionsView.getBorderless());
        configFile.setHardwareAcceleration(
            optionsView.getHardwareAcceleration());
        configFile.setFullscreen(optionsView.getFullscreen());
        configFile.setLargerFonts(optionsView.getLargerFonts());
        configFile.setResolution(gameFrame.getWidth(), gameFrame.getHeight());
        GuiStatics.setLargerFonts(configFile.getLargerFonts());
        configFile.setLightIntense(optionsView.getIntense());
        configFile.setAmbientLights(optionsView.isLightsEnabled());
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_CANCEL)) {
        MusicPlayer.setVolume(configFile.getMusicVolume());
        SoundPlayer.setSoundVolume(configFile.getSoundVolume());
        if (!isFullscreenMode()) {
          setNewResolution(configFile.getResolutionWidth() + "x"
              + configFile.getResolutionHeight());
        }
        SoundPlayer.playMenuSound();
        changeGameState(GameState.MAIN_MENU);
        if (gameFrame.isResizable() && !isFullscreenMode()) {
          setResizable(false);
        }
        return;
      }
      optionsView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.SETUP_AMBIENT_LIGHTS
        && ambientLightsView != null) {
      // Ambient lights setup
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_OK)) {
        SoundPlayer.playMenuSound();
        if (ambientLightsView.getBridge() != null) {
          Bridge tmpBridge = ambientLightsView.getBridge();
          if (tmpBridge.getHostname() != null
              && tmpBridge.getUsername() != null) {
            configFile.setBridgeHost(tmpBridge.getHostname());
            configFile.setBridgeId(tmpBridge.getId());
            configFile.setBridgeUsername(tmpBridge.getUsername());
            configFile.setLeftLight(ambientLightsView.getLeftLight());
            configFile.setRightLight(ambientLightsView.getRightLight());
            configFile.setCenterLight(ambientLightsView.getCenterLight());
            configFile.setLightIntense(ambientLightsView.getIntense());
            configFile.setAmbientLights(ambientLightsView.isLightsEnabled());
            setBridgeCommand(BridgeCommandType.EXIT);
            writeConfigFile();
            initBridge();
          }
        }
        changeGameState(GameState.OPTIONS_VIEW);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_CANCEL)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.OPTIONS_VIEW);
        return;
      }
      ambientLightsView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.MAIN_MENU
        || gameState == GameState.TEXT_SCREEN_VIEW) {
      // Main menu
      if (arg0.getActionCommand().equals(GameCommands.COMMAND_ANIMATION_TIMER)
          && mainMenu != null) {
        mainMenu.repaint();
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_CONTINUE_GAME)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.LOAD_GAME);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_NEW_GAME)) {
        galaxyConfig = null;
        SoundPlayer.playMenuSound();
        changeGameState(GameState.GALAXY_CREATION);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_CREDITS)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.CREDITS);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_OPTIONS_VIEW)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.OPTIONS_VIEW);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_QUIT_GAME)) {
        SoundPlayer.playMenuSound();
        if (setBridgeCommand(BridgeCommandType.WARM_WHITE)) {
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            // Nothing to do
          }
        }
        System.exit(0);
      }
    }

  }

  /**
   * Actions performed when state is star map
   * @param arg0 ActionEvent which has occured
   */
  private void actionPerformedStarMap(final ActionEvent arg0) {
    if (gameState == GameState.STARMAP && starMapView != null
        && starMapView.getPopup() != null) {
      starMapView.handleActions(arg0);
      if (!arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_ANIMATION_TIMER)) {
        SoundPlayer.playSound(SoundPlayer.MENU_DISABLED);
      }
      if (starMapView.getPopup().isDismissed()) {
        if (starMapView.getPopup().getCombat() != null) {
          starMapView.setReadyToMove(false);
          changeGameState(GameState.COMBAT,
              starMapView.getPopup().getCombat());
        }
        starMapView.setPopup(null);
        starMapView.updatePanels();
        return;
      }
    }
    if (gameState == GameState.STARMAP && starMapView != null
        && starMapView.getPopup() == null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_END_TURN)) {
        SoundPlayer.playMenuSound();
        new GameRepository()
        .saveGame(GameRepository.DEFAULT_SAVE_FOLDER, "autosave.save", starMap);
        changeGameState(GameState.AITURN);
      } else if (arg0.getActionCommand()
          .equals(GameCommands.COMMAND_FOCUS_MSG)) {
        SoundPlayer.playMenuSound();
        focusOnMessage(false);
      } else if (arg0.getActionCommand()
          .equals(GameCommands.COMMAND_PREV_TARGET)) {
        if (starMapView.getStarMapMouseListener()
            .getLastClickedFleet() != null) {
          Fleet fleet = players.getCurrentPlayerInfo().getFleets().getPrev();
          SoundPlayer.playMenuSound();
          changeMessageForFleets(fleet);
        } else if (starMapView.getStarMapMouseListener().getLastClickedPlanet()
            != null) {
          Planet planet = starMap.getNextPlanetForPlayer(starMap
              .getCurrentPlayerInfo(), starMapView.getStarMapMouseListener()
              .getLastClickedPlanet(), false);
          SoundPlayer.playMenuSound();
          changeMessageForPlanet(planet);
        }
      } else if (arg0.getActionCommand().equalsIgnoreCase(
          GameCommands.COMMAND_TRADE_FLEET)
          && starMapView.getStarMapMouseListener().getLastClickedFleet() != null
          && starMap.getPlayerInfoByFleet(
              starMapView.getStarMapMouseListener().getLastClickedFleet())
              == players.getCurrentPlayerInfo()) {
        changeGameState(GameState.FLEET_TRADE_VIEW);
        SoundPlayer.playMenuSound();
      } else if (arg0.getActionCommand()
          .equals(GameCommands.COMMAND_SPY)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.ESPIONAGE_VIEW);
      } else if (arg0.getActionCommand()
          .equals(GameCommands.COMMAND_NEXT_TARGET)) {
        if (starMapView.getStarMapMouseListener()
            .getLastClickedFleet() != null) {
          Fleet fleet = players.getCurrentPlayerInfo().getFleets().getNext();
          SoundPlayer.playMenuSound();
          changeMessageForFleets(fleet);
        } else if (starMapView.getStarMapMouseListener().getLastClickedPlanet()
            != null) {
          Planet planet = starMap.getNextPlanetForPlayer(starMap
              .getCurrentPlayerInfo(), starMapView.getStarMapMouseListener()
              .getLastClickedPlanet(), true);
          SoundPlayer.playMenuSound();
          changeMessageForPlanet(planet);
        }
      } else if (arg0.getActionCommand()
          .equals(GameCommands.COMMAND_SHOW_PLANET_LIST)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.PLANET_LIST_VIEW);
        return;
      } else {
        if (arg0.getActionCommand()
            .equalsIgnoreCase(GameCommands.COMMAND_ANIMATION_TIMER)) {
          handleDoubleClicksOnStarMap();
        }
        if (arg0.getActionCommand()
            .equalsIgnoreCase(GameCommands.COMMAND_VIEW_PLANET)
            && starMapView.getStarMapMouseListener()
                .getLastClickedPlanet() != null) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.PLANETVIEW);
        }
        if (arg0.getActionCommand()
            .equalsIgnoreCase(GameCommands.COMMAND_VIEW_FLEET)
            && starMapView.getStarMapMouseListener()
                .getLastClickedFleet() != null) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.FLEETVIEW);
        }
        if (arg0.getActionCommand()
            .equalsIgnoreCase(GameCommands.COMMAND_VIEW_RESEARCH)) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.RESEARCHVIEW);
        }
        if (arg0.getActionCommand()
            .equalsIgnoreCase(GameCommands.COMMAND_VIEW_HELP)) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.HELP_VIEW);
        }
        if (arg0.getActionCommand().equalsIgnoreCase(
            GameCommands.COMMAND_SHOW_HISTORY)) {
          // Debugging purposes
          SoundPlayer.playMenuSound();
          changeGameState(GameState.HISTORY_VIEW);
        }
        if (arg0.getActionCommand().equalsIgnoreCase(
            GameCommands.COMMAND_SHIPS)) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.VIEWSHIPS);
        }
        if (arg0.getActionCommand().equalsIgnoreCase(
            GameCommands.COMMAND_VIEW_STATS)) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.VIEWSTATS);
        }
        if (arg0.getActionCommand().equalsIgnoreCase(
            GameCommands.COMMAND_VIEW_VOTING)) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.VOTE_VIEW);
        }
        if (arg0.getActionCommand().equalsIgnoreCase(
            GameCommands.COMMAND_VIEW_LEADERS)) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.LEADER_VIEW);
        }
        if (arg0.getActionCommand().equalsIgnoreCase(
            GameCommands.COMMAND_BATTLE)) {
          // Debugging purposes
          SoundPlayer.playMenuSound();
          changeGameState(GameState.COMBAT);
        }
        starMapView.handleActions(arg0);
        if (starMapView.isAutoFocus()
            && arg0.getActionCommand().equals(GameCommands.COMMAND_END_TURN)) {
          SoundPlayer.playMenuSound();
          starMapView.setAutoFocus(false);
          focusOnMessage(true);
        }
      }
    }
  }
  @Override
  public void actionPerformed(final ActionEvent arg0) {
    if (arg0.getActionCommand() == GameCommands.COMMAND_MUSIC_TIMER) {
      if (songText != null) {
        if (MusicPlayer.isTextDisplayedEnough()) {
          songText.setVisible(false);
        } else {
          MusicFileInfo info = MusicPlayer.getNowPlaying();
          String text = "<html>Now playing:<br>" + info.getName() + "<br> by "
              + info.getAuthor() + "</html>";
          songText.setText(text);
          songText.setVisible(true);
        }
      }
      MusicPlayer.handleMusic(gameState);
      return;
    }
    if (gameState == GameState.FLEET_TRADE_VIEW && fleetTradeView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_START_TRADE_MISSION)
          && fleetTradeView.getTradeRoute() != null) {
        SoundPlayer.playMenuSound();
        TradeRoute route = fleetTradeView.getTradeRoute();
        Mission mission = new Mission(MissionType.TRADE_FLEET,
            MissionPhase.LOADING, route.getTradeWorld().getCoordinate());
        mission.setPlanetBuilding(route.getOriginWorld().getName());
        mission.setTargetPlanet(route.getTradeWorld().getName());
        mission.setFleetName(fleetTradeView.getFleet().getName());
        fleetTradeView.getPlayerInfo().getMissions().deleteMissionForFleet(
            fleetTradeView.getFleet().getName());
        fleetTradeView.getPlayerInfo().getMissions().add(mission);
        fleetTradeView =  null;
        changeGameState(GameState.STARMAP);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_STOP_TRADE_MISSION)) {
        SoundPlayer.playMenuSound();
        fleetTradeView.getPlayerInfo().getMissions().deleteMissionForFleet(
            fleetTradeView.getFleet().getName());
        fleetTradeView.getFleet().setRoute(null);
        fleetTradeView =  null;
        changeGameState(GameState.STARMAP);
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
    }
    if (gameState == GameState.STARMAP) {
      actionPerformedStarMap(arg0);
      return;
    }
    if (gameState == GameState.COMBAT && combatView != null) {
      if (combatView.isCombatEnded() && arg0.getActionCommand()
          .equals(GameCommands.COMMAND_END_BATTLE_ROUND)) {
        SoundPlayer.playMenuSound();
        MusicPlayer.playGameMusic();
        getStarMap().getFleetTiles(true);
        Combat combat = combatView.getCombat();
        getStarMap().getHistory().addEvent(combat.getCombatEvent());
        if (combat.getLeaderKilledNews() != null) {
          getStarMap().getNewsCorpData().addNews(combat.getLeaderKilledNews());
        }
        if (combat.getOrbitalDestoyedNews() != null) {
          getStarMap().getNewsCorpData().addNews(
              combat.getOrbitalDestoyedNews());
        }
        if (previousState == GameState.AITURN) {
          changeGameState(previousState);
          return;
        }
        changeGameState(GameState.STARMAP);
        if (combat.getWinnerFleet() != null) {
          getStarMap().doFleetScanUpdate(combat.getWinner(),
              combat.getWinnerFleet(), null);
        }
        return;
      }
      combatView.handleActions(arg0);
      return;
    }
    if (gameState == GameState.HISTORY_VIEW && historyView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_DONE_HISTORY)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.VIEWSTATS);
        return;
      }
      historyView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.HELP_VIEW && helpView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        starMap.setTutorialEnabled(helpView.isTutorialEnabled());
        changeGameState(GameState.STARMAP);
        return;
      }
      //FIXME Do handling for help view
      return;
    }
    if (gameState == GameState.VOTE_VIEW && voteView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
      voteView.handleActions(arg0);
      return;
    }
    if (gameState == GameState.REALM_VIEW && realmView != null
        && arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
      SoundPlayer.playMenuSound();
      changeGameState(previousState);
      return;
    }
    if (gameState == GameState.PLANET_LIST_VIEW && planetListView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
      if (arg0.getActionCommand()
          .contains(GameCommands.COMMAND_VIEW_PLANET)) {
        String[] temp = arg0.getActionCommand().split("\\|");
        Planet planet = starMap.getPlanetByName(temp[0]);
        if (planet != null) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.PLANETVIEW, planet);
          return;
        }
      }
      planetListView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.PLANETBOMBINGVIEW && planetBombingView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        planetBombingView.handleLastNewsAndReputation();
        if (previousState == GameState.AITURN) {
          changeGameState(previousState);
          return;
        }
        changeGameState(GameState.STARMAP);
        return;
      }
      planetBombingView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.AITURN && aiTurnView != null) {
      aiTurnView.handleActions(arg0);
    }
    if (gameState == GameState.RESEARCHVIEW && researchView != null) {
      // Handle Research View
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
      researchView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.LEADER_VIEW && leaderView != null) {
      // Handle Leader View
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
      leaderView.handleActions(arg0);
      return;
    }
    if (gameState == GameState.VIEWSHIPS && shipView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
      if (arg0.getActionCommand().equalsIgnoreCase(
          GameCommands.COMMAND_DELETE_SHIP)) {
        ShipStat stat = shipView.getSelectedStat();
        PlayerInfo builder = shipView.getPlayerInfo();
        if (!starMap.isShipStatBeingBuilt(stat, builder)) {
          builder.removeShipStat(stat);
          SoundPlayer.playMenuSound();
          shipView.updateList();
        }
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_COPY_SHIP)) {
        SoundPlayer.playMenuSound();
        shipView.setCopyClicked(true);
        changeGameState(GameState.SHIPDESIGN);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_SHIPDESIGN)) {
        shipView.setCopyClicked(false);
        SoundPlayer.playMenuSound();
        changeGameState(GameState.SHIPDESIGN);
        return;
      }
      // Handle View Ship
      shipView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.DIPLOMACY_VIEW && diplomacyView != null) {
      // Handle diplomacy view
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        diplomacyView.updateMeetingNumbers();
        if (!diplomacyView.didTradeHappen()) {
          // Human and AI player had nothing to trade, so
          // adding nothing to trade bonus
          diplomacyView.addNothingToTrade();
        }
        if (previousState == GameState.AITURN) {
          changeGameState(previousState);
          return;
        }
        changeGameState(GameState.STARMAP);
        return;
      }
      diplomacyView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.SHIPDESIGN && shipDesignView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_SHIPDESIGN_DONE)
          && shipDesignView != null && shipDesignView.isDesignOK()) {
        SoundPlayer.playMenuSound();
        shipDesignView.keepDesign();
        changeGameState(GameState.VIEWSHIPS);
        return;
      }
      if (arg0.getActionCommand().equalsIgnoreCase(
          GameCommands.COMMAND_SHIPS)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.VIEWSHIPS);
        return;
      }
      // Ship Design View
      shipDesignView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.ESPIONAGE_VIEW && espionageView != null) {
      // Espionage  View
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
      if (arg0.getActionCommand()
          .contains(GameCommands.COMMAND_REALM_VIEW)) {
        String[] temp = arg0.getActionCommand().split("\\|");
        PlayerInfo realm = starMap.getPlayerList().findByName(temp[0]);
        if (realm != null) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.REALM_VIEW, realm);
          return;
        }
      }
      espionageView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.NEWS_CORP_VIEW && newsCorpView != null) {
      // News Corp view
      newsCorpView.handleAction(arg0);
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        if (starMap.isGameEnded()) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.HISTORY_VIEW);
          return;
        }
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
    }
    if (gameState == GameState.VIEWSTATS && statView != null) {
      // Stat View
      statView.handleAction(arg0);
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        if (starMap.isGameEnded()) {
          SoundPlayer.playMenuSound();
          changeGameState(GameState.MAIN_MENU);
          return;
        }
        SoundPlayer.playMenuSound();
        changeGameState(GameState.STARMAP);
        return;
      }
    }
    if (gameState == GameState.PLANETVIEW && planetView != null) {
      // Planet view
      if (arg0.getActionCommand().equals(
          GameCommands.COMMAND_HAIL_FLEET_PLANET)) {
        changeGameState(GameState.DIPLOMACY_VIEW, planetView);
        SoundPlayer.playSound(SoundPlayer.RADIO_CALL);
      }
      if (arg0.getActionCommand().equals(
          GameCommands.COMMAND_VIEW_LEADERS)) {
        changeGameState(GameState.LEADER_VIEW, planetView.getPlanet());
        SoundPlayer.playMenuSound();
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        planetView = null;
        if (previousState == GameState.PLANET_LIST_VIEW) {
          changeGameState(previousState);
        } else {
          changeGameState(GameState.STARMAP);
        }
        return;
      }
      planetView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.ESPIONAGE_MISSIONS_VIEW
        && espionageMissionView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_FLEET)) {
        changeGameState(GameState.FLEETVIEW, espionageMissionView.getFleet());
        SoundPlayer.playMenuSound();
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_HAIL_FLEET_PLANET)) {
        changeGameState(GameState.DIPLOMACY_VIEW,
            espionageMissionView.getPlanet());
        SoundPlayer.playSound(SoundPlayer.RADIO_CALL);
        return;
      }
      espionageMissionView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.SHIP_UPGRADE_VIEW && shipUpgradeView != null) {
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_FLEET)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.FLEETVIEW);
        return;
      }
      shipUpgradeView.handleAction(arg0);
    }
    if (gameState == GameState.FLEETVIEW && fleetView != null) {
      // Fleet view
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_STARMAP)) {
        SoundPlayer.playMenuSound();
        Fleet fleet = fleetView.getFleet();
        fleetView = null;
        changeGameState(GameState.STARMAP, fleet);
        return;
      }
      if (arg0.getActionCommand()
          .equalsIgnoreCase(GameCommands.COMMAND_VIEW_UPGRADE)) {
        SoundPlayer.playMenuSound();
        changeGameState(GameState.SHIP_UPGRADE_VIEW);
        return;
      }
      if (arg0.getActionCommand().equals(
          GameCommands.COMMAND_ESPIONAGE_MISSIONS)) {
        changeGameState(GameState.ESPIONAGE_MISSIONS_VIEW,
            fleetView.getEspionagePlanet());
        SoundPlayer.playMenuSound();
      }
      if (arg0.getActionCommand().equals(
          GameCommands.COMMAND_VIEW_LEADERS)) {
        changeGameState(GameState.LEADER_VIEW, fleetView.getFleet());
        SoundPlayer.playMenuSound();
      }
      if (arg0.getActionCommand().equals(GameCommands.COMMAND_COLONIZE)) {
        SoundPlayer.playMenuSound();
        colonizePlanetAction();
        return;
      }
      if (arg0.getActionCommand().equals(GameCommands.COMMAND_CONQUEST)) {
        Route route = fleetView.getFleet().getRoute();
        if (route == null) {
          Fleet fleet = fleetView.getFleet();
          fleet.setRoute(new Route(fleet.getX(), fleet.getY(), fleet.getX(),
              fleet.getY(), Route.ROUTE_BOMBED));
          fleet.setMovesLeft(0);
          changeGameState(GameState.PLANETBOMBINGVIEW, fleetView);
          SoundPlayer.playMenuSound();
          return;
        }
        SoundPlayer.playMenuDisabled();
      }
      if (arg0.getActionCommand().equals(
          GameCommands.COMMAND_HAIL_FLEET_PLANET)) {
        Fleet fleet = fleetView.getFleet();
        CulturePower power = starMap.getSectorCulture(fleet.getX(),
            fleet.getY());
        SoundPlayer.playSound(SoundPlayer.RADIO_CALL);
        if (power.getHighestCulture() == starMap.getPlayerList()
            .getCurrentPlayer()) {
          changeGameState(GameState.DIPLOMACY_VIEW, fleet);
        } else {
          changeGameState(GameState.DIPLOMACY_VIEW, fleetView);
        }
        return;
      }
      fleetView.handleAction(arg0);
      return;
    }
    if (gameState == GameState.GALAXY_CREATION
        || gameState == GameState.PLAYER_SETUP
        || gameState == GameState.SAVE_GAME_NAME_VIEW
        || gameState == GameState.LOAD_GAME
        || gameState == GameState.OPTIONS_VIEW
        || gameState == GameState.SETUP_AMBIENT_LIGHTS
        || gameState == GameState.MAIN_MENU
        || gameState == GameState.TEXT_SCREEN_VIEW
        || gameState == GameState.CREDITS) {
      actionPerformedMenus(arg0);
    }
  }

  /**
   * Get the list of players
   * @return the list of players
   */
  public PlayerList getPlayers() {
    return players;
  }

  /**
   * Get the current Game state
   * @return the current Game state
   */
  public GameState getGameState() {
    return gameState;
  }

  /**
   * Get the StarMap view for the game
   * @return the StarMap view for the game
   */
  public StarMapView getStarMapView() {
    return starMapView;
  }

  /**
   * Set the StarMap view for the game.
   * @param view StarMapView to set
   */
  public void setStarMapView(final StarMapView view) {
    starMapView = view;
  }

  /**
   * Set loaded starmap into game
   * @param map Starmap to set game
   * @return true if succesful false otherwise
   */
  public boolean setLoadedGame(final StarMap map) {
    if (map != null) {
      starMap = map;
      players = starMap.getPlayerList();
      if (tutorialList != null) {
        tutorialList.updateShownTutorial(starMap.getShownTutorialIndexes());
      }
      starMap.updateStarMapOnLoadGame();
      return true;
    }
    return false;
  }

  /**
   * Get AI Turn view
   * @return AITurnView
   */
  public AITurnView getAITurnView() {
    return aiTurnView;
  }
  /**
   * Set AI Turn view
   * @param view AITurnVIew
   */
  public void setAITurnView(final AITurnView view) {
    aiTurnView = view;
  }

  /**
   * Set Galaxy config
   * @param config Galaxy config
   */
  public void setGalaxyConfig(final GalaxyConfig config) {
    galaxyConfig = config;
  }

  /**
   * Is show minimap flag enabled
   * @return True if minimap should be showing
   */
  public boolean isShowMiniMapFlag() {
    return showMiniMapFlag;
  }

  /**
   * Set minimap showing flag.
   * @param showMiniMapFlag true to show minimap.
   */
  public void setShowMiniMapFlag(final boolean showMiniMapFlag) {
    this.showMiniMapFlag = showMiniMapFlag;
  }

  /**
   * Get last set save game file name.
   * @return Save game file name.
   */
  public String getSaveGameFile() {
    return saveFilename;
  }

  /**
   * Get Tutorial list.
   * @return Tutorial list
   */
  public static TutorialList getTutorial() {
    return tutorialList;
  }
}
