package com.hughie.dandao.common.screen;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.client.network.AlchemyFurnaceDataPacket;
import net.minecraft.client.gui.components.Button;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlchemyFurnaceScreen extends AbstractContainerScreen<AlchemyFurnaceMenu> {
    private final static ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DanDao.MOD_ID, "textures/gui/alchemy_furnace_gui.png");
    private static final int IMAGE_WIDTH = 181;
    private static final int IMAGE_HEIGHT = 193;

    private Button toggleButton;

    public AlchemyFurnaceScreen(AlchemyFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = IMAGE_HEIGHT;
        this.imageWidth = IMAGE_WIDTH;
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 1000;
        this.titleLabelY = 1000;

        int screenX = (this.width - this.imageWidth) / 2;
        int screenY = (this.height - this.imageHeight) / 2;

        toggleButton = Button.builder(getButtonTextFromMenu(),
                         (button) -> this.onButtonClick()).bounds(screenX + 110, screenY + 87, 25, 15).build();

        this.addRenderableWidget(toggleButton);
    }

    private Component getButtonTextFromMenu() {
        int state = menu.getButtonState();
        return state == 1 ? Component.translatable("menu.dandao.close_furnace") : Component.translatable("menu.dandao.open_furnace");
    }

    private void onButtonClick() {
        DanDao.NETWORK.sendToServer(new AlchemyFurnaceDataPacket(menu.alchemyFurnaceBlockEntity.getBlockPos()));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderBurningBar(guiGraphics, x, y);
        renderHeatBar(guiGraphics, x, y);
        renderCoolingBar(guiGraphics, x, y);
    }

    private void renderBurningBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isBurning()) {
            int scaled = menu.getScaledBurningProcess();
            int barY = y + 22 + 78 - scaled;
            guiGraphics.blit(TEXTURE, x + 28, barY, 185, 3 + 79 - scaled, 5, scaled);
        }
    }

    private void renderHeatBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isHeated()) {
            int scaled = menu.getScaledHeatProcess();
            int barY = y + 21 + 79 - scaled;
            guiGraphics.blit(TEXTURE, x + 36, barY, 193, 3 + 78 - scaled, 17, scaled);
        }
    }

    private void renderCoolingBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCooling()) {
            int scaled = menu.getScaledCoolingProcess();
            int barY = y + 22 + 79 - scaled;
            guiGraphics.blit(TEXTURE, x + 28, barY, 199, 96 + 79 - scaled, 5, scaled);
        }
    }

    private void renderLockIcon(GuiGraphics guiGraphics, int x, int y) {
        if(menu.getButtonState() == 0) {
            for(int i = 0; i < 4; i ++) {
                guiGraphics.blit(TEXTURE, x + 72, y + 24 + (i * 20), 222, 43, 11, 16);
            }
            for(int i = 0; i < 4; i ++) {
                guiGraphics.blit(TEXTURE, x + 165, y + 24 + (i * 20), 222, 43, 11, 16);
            }
        }
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        toggleButton.setMessage(getButtonTextFromMenu());
        renderLockIcon(guiGraphics, getGuiLeft(), getGuiTop());
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (isMouseOverHeatBar(mouseX, mouseY)) {
            int heat = menu.getData().get(2);
            int maxHeat = menu.getData().get(3);
            Component text = Component.translatable("tooltip.dandao.heat_progress", heat, maxHeat);
            guiGraphics.renderTooltip(this.font, text, mouseX, mouseY);
        }
    }

    private boolean isMouseOverHeatBar(int mouseX, int mouseY) {
        int x = getGuiLeft();
        int y = getGuiTop();
        return mouseX >= x + 36 && mouseX <= x + 36 + 17
                && mouseY >= y + 21 && mouseY <= y + 21 + 79;
    }

}
