package me.alpha432.oyvey.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.uop;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.*;
import me.alpha432.oyvey.features.modules.combat.*;
import me.alpha432.oyvey.features.modules.misc.*;
import me.alpha432.oyvey.features.modules.movement.*;
import me.alpha432.oyvey.features.modules.player.*;
import me.alpha432.oyvey.features.modules.render.*;
import me.alpha432.oyvey.features.modules.hud.*;
import me.alpha432.oyvey.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Feature {
    public java.util.ArrayList<Module> modules = new java.util.ArrayList();
    public List<Module> sortedModules = new java.util.ArrayList<Module>();
    public List<String> sortedModulesABC = new java.util.ArrayList<String>();
    public Animation animationThread;

    public void init() {
        //hud
        this.modules.add(new ArmourHUD());
        this.modules.add(new ArrayListJ());
        this.modules.add(new BetterPotions());
        this.modules.add(new Coords());
        this.modules.add(new ConnectionInfo());
        this.modules.add(new Dashboard());
        this.modules.add(new Radar());
        this.modules.add(new Totems());
        this.modules.add(new Watermark());
        this.modules.add(new Welcomer());

        // client
        this.modules.add(new ClickGui());
        this.modules.add(new Debug());
        this.modules.add(new FontMod());
        this.modules.add(new HUD());
        this.modules.add(new MainMenu());
        
        // combat
        this.modules.add(new AntiDesyncC());
        this.modules.add(new AntiInvis());
        this.modules.add(new AntiUnicode());
        this.modules.add(new AutoTrap());
        this.modules.add(new CallBackup());
        this.modules.add(new CAMessage());
        this.modules.add(new CrystalPredict());
        this.modules.add(new FCAIALM());
        this.modules.add(new Selftrap());
        
        // misc
        this.modules.add(new AllCaps());
        this.modules.add(new AutoConfig());
        this.modules.add(new AutoJewbase());
        this.modules.add(new BuildHeight());
        this.modules.add(new ChatModifier());
        this.modules.add(new CustomTime());
        this.modules.add(new ExtraTab());
        this.modules.add(new FakeDuelMessage());
        this.modules.add(new GoodLuckCharm());
        this.modules.add(new PacketLogger());
        this.modules.add(new PopCounter());
        this.modules.add(new ToolTips());
        this.modules.add(new XormiosModule());
        
        // movement
        this.modules.add(new HorizonBhop());
        this.modules.add(new InstantSpeed());
        this.modules.add(new LagBack());
        this.modules.add(new NoClip());
        this.modules.add(new ReverseStep());
        this.modules.add(new WebSpeed());
        
        // player
        this.modules.add(new ChestSwap());
        this.modules.add(new ElytraDeploy());
        this.modules.add(new GMSwitcher());
        this.modules.add(new Instamine());
        this.modules.add(new LeftHand());
        this.modules.add(new MCP());
        this.modules.add(new MultiTask());
        this.modules.add(new NoTrace());
        this.modules.add(new Swing());
        
        // render
        this.modules.add(new AA());
        this.modules.add(new ArrowESP());
        this.modules.add(new AspectRatio());
        this.modules.add(new Background());
        this.modules.add(new BlackBars());
        this.modules.add(new BlockHighlight());
        this.modules.add(new BurrowESP());
        this.modules.add(new CrystalTweaks());
        this.modules.add(new DeathEffects());
        this.modules.add(new DebugCrosshair());
        this.modules.add(new ESP());
        this.modules.add(new FutureVM());
        this.modules.add(new GlintTweaks());
        this.modules.add(new HandChams());
        this.modules.add(new HoleESP());
        this.modules.add(new ItemScale());
        this.modules.add(new LogSpots());
        this.modules.add(new NoSway());
        this.modules.add(new OldAnimations());
        this.modules.add(new PlayerTweaks());
        this.modules.add(new RusherCapes());
        this.modules.add(new Skeleton());
        this.modules.add(new SkyColor());
        this.modules.add(new SmallShield());
        this.modules.add(new Trajectories());
        this.modules.add(new Wireframe());
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public java.util.ArrayList<Module> getEnabledModules() {
        java.util.ArrayList<Module> enabledModules = new java.util.ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public java.util.ArrayList<String> getEnabledModulesName() {
        java.util.ArrayList<String> enabledModules = new java.util.ArrayList<String>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public java.util.ArrayList<Module> getModulesByCategory(Module.Category category) {
        java.util.ArrayList<Module> modulesCategory = new java.util.ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus) MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new java.util.ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof OyVeyGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = uop.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}

