package com.soybean.event.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.soybean.config.InitValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class EventHud {

    // 是否显示全屏白色闪烁效果
    private static boolean showFlash = false;
    // 闪烁效果的持续时间
    private static int flashDuration = 0;
    // 闪烁效果的最大持续时间（ticks）
    private static final int MAX_FLASH_DURATION = 20;
    // 闪烁效果的不透明度
    private static float flashOpacity = 1.0f;

    public static void init() {
        HudRenderCallback.EVENT.register((drawContext, tickDeltaManager) -> {
            // 如果需要显示闪烁效果
            if (showFlash) {
                // 获取Minecraft客户端实例
                MinecraftClient client = MinecraftClient.getInstance();
                
                // 计算闪烁效果的不透明度（从1.0逐渐降到0）
                flashOpacity = Math.max(0, (float)(MAX_FLASH_DURATION - flashDuration) / MAX_FLASH_DURATION);
                
                // 根据不透明度设置白色闪烁
                renderFullScreenFlash(drawContext, 1.0f, 1.0f, 1.0f, flashOpacity);
                
                // 减少闪烁持续时间
                flashDuration++;
                
                // 如果闪烁效果结束，重置状态
                if (flashDuration >= MAX_FLASH_DURATION) {
                    resetFlash();
                }
            }
        });
    }

    /**
     * 触发全屏白色闪烁效果
     */
    public static void triggerFlash() {
        showFlash = true;
        flashDuration = 0;
        flashOpacity = 1.0f;
    }
    
    /**
     * 重置闪烁效果
     */
    private static void resetFlash() {
        showFlash = false;
        flashDuration = 0;
    }
    
    /**
     * 渲染全屏颜色遮罩
     * @param drawContext 绘制上下文
     * @param r 红色分量 (0-1)
     * @param g 绿色分量 (0-1)
     * @param b 蓝色分量 (0-1)
     * @param alpha 不透明度 (0-1)
     */
    private static void renderFullScreenFlash(DrawContext drawContext, float r, float g, float b, float alpha) {
        // 获取窗口尺寸
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        
        // 设置RenderSystem属性
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        
        // 绘制全屏矩形，使用指定颜色和不透明度
        drawContext.fill(0, 0, width, height, ((int)(alpha * 255) << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255));
        
        // 恢复RenderSystem状态
        RenderSystem.enableDepthTest();
    }
}
