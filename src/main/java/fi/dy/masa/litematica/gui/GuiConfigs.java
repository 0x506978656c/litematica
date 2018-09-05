package fi.dy.masa.litematica.gui;

import java.util.Collections;
import java.util.List;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiConfigs extends GuiConfigsBase
{
    private int id;

    public GuiConfigs()
    {
        super(10, 50, null);

        this.title = I18n.format("litematica.gui.title.configs");
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.clearOptions();

        this.id = 0;
        int x = 10;
        int y = 26;

        x += this.createButton(x, y, -1, ConfigGuiTab.GENERIC) + 4;
        x += this.createButton(x, y, -1, ConfigGuiTab.VISUALS) + 4;
        x += this.createButton(x, y, -1, ConfigGuiTab.COLORS) + 4;
        x += this.createButton(x, y, -1, ConfigGuiTab.HOTKEYS) + 4;
        x += this.createButton(x, y, -1, ConfigGuiTab.RENDER_LAYERS) + 4;
    }

    private int createButton(int x, int y, int width, ConfigGuiTab tab)
    {
        ButtonListener listener = new ButtonListener(tab, this);
        boolean enabled = DataManager.getConfigGuiTab() != tab;
        String label = tab.getDisplayName();

        if (width < 0)
        {
            width = this.mc.fontRenderer.getStringWidth(label) + 10;
        }

        ButtonGeneric button = new ButtonGeneric(this.id++, x, y, width, 20, label);
        button.enabled = enabled;
        this.addButton(button, listener);

        return width;
    }

    @Override
    public List<? extends IConfigValue> getConfigs()
    {
        ConfigGuiTab tab = DataManager.getConfigGuiTab();

        if (tab == ConfigGuiTab.GENERIC)
        {
            return Configs.Generic.OPTIONS;
        }
        else if (tab == ConfigGuiTab.VISUALS)
        {
            return Configs.Visuals.OPTIONS;
        }
        else if (tab == ConfigGuiTab.COLORS)
        {
            return Configs.Colors.OPTIONS;
        }
        else if (tab == ConfigGuiTab.HOTKEYS)
        {
            return Hotkeys.HOTKEY_LIST;
        }

        return Collections.emptyList();
    }

    private static class ButtonListener implements IButtonActionListener<ButtonGeneric>
    {
        private final GuiConfigs parent;
        private final ConfigGuiTab tab;

        public ButtonListener(ConfigGuiTab tab, GuiConfigs parent)
        {
            this.tab = tab;
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ButtonGeneric control)
        {
        }

        @Override
        public void actionPerformedWithButton(ButtonGeneric control, int mouseButton)
        {
            DataManager.setConfigGuiTab(this.tab);

            if (this.tab != ConfigGuiTab.RENDER_LAYERS)
            {
                this.parent.widget.resetScrollbarPosition();
                this.parent.initGui();
            }
            else
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiRenderLayer());
            }
        }
    }

    public enum ConfigGuiTab
    {
        GENERIC         ("litematica.gui.button.config_gui.generic"),
        VISUALS         ("litematica.gui.button.config_gui.visuals"),
        COLORS          ("litematica.gui.button.config_gui.colors"),
        HOTKEYS         ("litematica.gui.button.config_gui.hotkeys"),
        RENDER_LAYERS   ("litematica.gui.button.config_gui.render_layers");

        private final String translationKey;

        private ConfigGuiTab(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public String getDisplayName()
        {
            return I18n.format(this.translationKey);
        }
    }
}