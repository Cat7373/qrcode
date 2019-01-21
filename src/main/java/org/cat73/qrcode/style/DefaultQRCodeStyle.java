package org.cat73.qrcode.style;

import lombok.NonNull;

import java.awt.image.BufferedImage;

/**
 * 默认的二维码样式 - 纯色样式
 */
public final class DefaultQRCodeStyle implements IQRCodeStyle {
    @Override
    public BufferedImage toImg(@NonNull boolean[][] arr, int borderBlock, int blockSize, int foregroundColor, int backgroundColor) {
        // 宽高
        int width = arr.length;

        // 结果图片
        BufferedImage image = new BufferedImage(width * blockSize, width * blockSize, BufferedImage.TYPE_INT_RGB);

        // 填充内容
        for (var y = 0; y < width; y++) {
            for (var x = 0; x < width; x++) {
                boolean foreground = arr[y][x];
                var color = foreground ? foregroundColor : backgroundColor;
                this.drawRect(image, x * blockSize, y * blockSize, blockSize, blockSize, color);
            }
        }

        // 返回结果
        return image;
    }

    /**
     * 将图片的指定区域绘制成指定颜色
     * @param image 被绘制的图片
     * @param x 区域开始的横坐标
     * @param y 区域开始的纵坐标
     * @param width 区域的宽度
     * @param height 区域的高度
     * @param color 绘制的颜色
     */
    private void drawRect(BufferedImage image, int x, int y, int width, int height, int color) {
        for (var posX = x + width - 1; posX >= x; posX--) {
            for (var posY = y + height - 1; posY >= y; posY--) {
                image.setRGB(posX, posY, color);
            }
        }
    }
}
