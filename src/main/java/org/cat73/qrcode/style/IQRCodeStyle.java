package org.cat73.qrcode.style;

import lombok.NonNull;

import java.awt.image.BufferedImage;

/**
 * 二维码样式的接口
 */
public interface IQRCodeStyle {
    /**
     * 生成二维码图片
     * @param arr 填充数组
     * @param borderBlock 边框宽度(方块数)
     * @param blockSize 块大小(像素)
     * @param foregroundColor 前景色(RGB)
     * @param backgroundColor 背景色(RGB)
     * @return 生成的图片，宽高应该等于 块大小 * 块数量
     */
    BufferedImage toImg(@NonNull boolean[][] arr, int borderBlock, int blockSize, int foregroundColor, int backgroundColor);
}
