package org.cat73.qrcode.style;

import lombok.NonNull;
import org.cat73.qrcode.util.Lang;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 将特定图片改造为二维码的样式<br>
 * 使用的图片中尽量不要有太接近前景色的颜色
 */
public final class ImgQRCodeStyle implements IQRCodeStyle {
    /**
     * 用于生成二维码的图片
     */
    private final BufferedImage img;
    /**
     * 边框部分是否绘制背景图
     */
    private final boolean imgBorder;
    /**
     * 绘制的小点占块大小的比例(5% ~ 100%)
     */
    private final double blockSizeRate;
    /**
     * 绘制点自适应颜色比例(0% ~ 100%)<br>
     * 以降低识别率为代价，让绘制的颜色更贴近原图<br>
     */
    private final double adaptiveColorRate;
    /**
     * 码眼自适应颜色比例(0% ~ 100%)<br>
     * 以降低识别率为代价，让绘制的颜色更贴近原图<br>
     */
    private final double eyeAdaptiveColorRate;

    // TODO 允许一定比例的误差(在纠错级别之内时，应仍能正常识别，这样尽量避免将图片画到差异太大的颜色)

    /**
     * 通过图片构造一个自身实例
     * @param img 图片
     * @param imgBorder 边框部分是否绘制背景图
     * @param blockSizeRate 绘制的小点占块大小的比例(5% ~ 100%)
     * @param adaptiveColorRate 绘制点自适应颜色比例(0% ~ 100%)
     * @param eyeAdaptiveColorRate 码眼自适应颜色比例(0% ~ 100%)
     */
    private ImgQRCodeStyle(@NonNull BufferedImage img, boolean imgBorder, double blockSizeRate, double adaptiveColorRate, double eyeAdaptiveColorRate) {
        this.img = img;
        this.imgBorder = imgBorder;
        this.blockSizeRate = blockSizeRate;
        this.adaptiveColorRate = adaptiveColorRate;
        this.eyeAdaptiveColorRate = eyeAdaptiveColorRate;
    }

    public static ImgQRCodeStyleBuilder builder() {
        return new ImgQRCodeStyleBuilder();
    }

    public static final class ImgQRCodeStyleBuilder {
        /**
         * 用于生成二维码的图片
         */
        private BufferedImage img;
        /**
         * 边框部分是否绘制背景图
         */
        private boolean imgBorder = true;
        /**
         * 绘制的小点占块大小的比例(5% ~ 100%)
         */
        private double blockSizeRate = 0.20;
        /**
         * 绘制点自适应颜色比例(0% ~ 100%)<br>
         * 以降低识别率为代价，让绘制的颜色更贴近原图<br>
         */
        private double adaptiveColorRate = 0.00;
        /**
         * 码眼自适应颜色比例(0% ~ 100%)<br>
         * 以降低识别率为代价，让绘制的颜色更贴近原图<br>
         */
        private double eyeAdaptiveColorRate = 0.00;

        /**
         * 使用图片作为背景图
         * @param img 图片
         * @return 自身实例，方便链式调用
         */
        public ImgQRCodeStyleBuilder img(@NonNull BufferedImage img) {
            this.img = img;
            return this;
        }

        /**
         * 使用图片作为背景图
         * @param in 图片的输入流
         * @return 自身实例，方便链式调用
         */
        public ImgQRCodeStyleBuilder img(@NonNull InputStream in) {
            return Lang.wrapCode(() -> this.img(ImageIO.read(in)));
        }

        /**
         * 使用图片作为背景图
         * @param file 图片的文件
         * @return 自身实例，方便链式调用
         */
        public ImgQRCodeStyleBuilder img(@NonNull File file) {
            return Lang.wrapCode(() -> this.img(new FileInputStream(file)));
        }

        /**
         * 使用图片作为背景图
         * @param filePath 图片的文件路径
         * @return 自身实例，方便链式调用
         */
        public ImgQRCodeStyleBuilder img(@NonNull String filePath) {
            return this.img(new File(filePath));
        }

        /**
         * 使用 ClassPath 中的图片作为背景图
         * @param path 图片在 ClassPath 中的路径
         * @return 自身实例，方便链式调用
         */
        public ImgQRCodeStyleBuilder classpathImg(@NonNull String path) {
            return this.img(ImgBlockQRCodeStyle.class.getResourceAsStream(path));
        }

        public ImgQRCodeStyleBuilder imgBorder() {
            this.imgBorder = true;
            return this;
        }

        public ImgQRCodeStyleBuilder colorBorder() {
            this.imgBorder = false;
            return this;
        }

        public ImgQRCodeStyleBuilder blockSizeRate(double rate) {
            if (rate < 0.05) throw Lang.makeThrow("rate < 5%");
            if (rate > 1.00) throw Lang.makeThrow("rate > 100%");

            this.blockSizeRate = rate;
            return this;
        }

        public ImgQRCodeStyleBuilder adaptiveColorRate(double rate) {
            if (rate < 0.00) throw Lang.makeThrow("rate < 0%");
            if (rate > 1.00) throw Lang.makeThrow("rate > 100%");

            this.adaptiveColorRate = rate;
            return this;
        }

        public ImgQRCodeStyleBuilder eyeAdaptiveColorRate(double rate) {
            if (rate < 0.00) throw Lang.makeThrow("rate < 0%");
            if (rate > 1.00) throw Lang.makeThrow("rate > 100%");

            this.eyeAdaptiveColorRate = rate;
            return this;
        }

        public ImgQRCodeStyle build() {
            return new ImgQRCodeStyle(this.img, this.imgBorder, this.blockSizeRate, this.adaptiveColorRate, this.eyeAdaptiveColorRate);
        }
    }

    @Override
    public BufferedImage toImg(@NonNull boolean[][] arr, int borderBlock, int blockSize, int foregroundColor, int backgroundColor) {
        // 宽高
        int blockWidth = arr.length;
        int imgWidth = blockWidth * blockSize;

        // 结果图片
        BufferedImage image = new BufferedImage(imgWidth, imgWidth, BufferedImage.TYPE_INT_RGB);
        // 获取画笔
        Graphics2D gs = image.createGraphics();

        // 画背景图
        if (this.imgBorder) {
            gs.drawImage(this.img, 0, 0, imgWidth, imgWidth, null);
        } else {
            gs.setBackground(new Color(backgroundColor));
            gs.clearRect(0, 0, imgWidth, imgWidth);
            gs.drawImage(this.img, borderBlock * blockSize, borderBlock * blockSize, (blockWidth - borderBlock - borderBlock) * blockSize, (blockWidth - borderBlock - borderBlock) * blockSize, null);
        }

        // 销毁 Graphics，释放资源
        // 如果上面的代码出现异常，会导致这行代码无法被调用到
        // 但 JVM 最终仍会执行这个方法同样的流程去回收资源，因此无需做特殊处理
        gs.dispose();

        // 填充内容
        var pointSize = (int) (blockSize * this.blockSizeRate);
        if (pointSize % 2 == 1) pointSize += 1;
        pointSize = Math.min(Math.max(pointSize, 2), blockSize);
        var pointStart = (blockSize - pointSize) / 2;

        // 绘制内容
        var end = blockWidth - borderBlock;
        for (var y = borderBlock; y < end; y++) {
            for (var x = borderBlock; x < end; x++) {
                boolean foreground = arr[y][x];
                var color = foreground ? foregroundColor : backgroundColor;
                this.drawRect(image, x * blockSize + pointStart, y * blockSize + pointStart, pointSize, pointSize, color, this.adaptiveColorRate);
            }
        }

        // 画三个码眼
        // 左上
        for (var x = borderBlock + 6; x >= borderBlock; x--) {
            for (var y = borderBlock + 6; y >= borderBlock; y--) {
                this.drawRect(image, x * blockSize, y * blockSize, blockSize, blockSize, arr[x][y] ? foregroundColor : backgroundColor, this.eyeAdaptiveColorRate);
                arr[x][y] = false;
            }
        }
        // 左下、右上
        for (var a = blockWidth - borderBlock - 7; a < end; a++) { // 横轴或纵轴数字较大的那边的坐标
            for (var b = borderBlock + 6; b >= borderBlock; b--) { // 与外循环的轴垂直的轴的坐标
                this.drawRect(image, a * blockSize, b * blockSize, blockSize, blockSize, arr[a][b] ? foregroundColor : backgroundColor, this.eyeAdaptiveColorRate);
                this.drawRect(image, b * blockSize, a * blockSize, blockSize, blockSize, arr[b][a] ? foregroundColor : backgroundColor, this.eyeAdaptiveColorRate);
                arr[a][b] = false; // 左下
                arr[b][a] = false; // 右上
            }
        }

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
     * @param adaptiveColorRate 自适应颜色的比例
     */
    private void drawRect(BufferedImage image, int x, int y, int width, int height, int color, double adaptiveColorRate) {
        // 计算实际使用的颜色
        int newColor;
        if (adaptiveColorRate - 0.00 > 1e-9) {
            // 求平均颜色
            int r = 0;
            int g = 0;
            int b = 0;
            for (var posX = width + x - 1; posX >= x; posX--) {
                for (var posY = height + y - 1; posY >= y; posY--) {
                    var c = image.getRGB(posX, posY);
                    r += (c >> 16) & 0xFF;
                    g += (c >> 8) & 0xFF;
                    b += c & 0xFF;
                }
            }
            r /= (width * height);
            g /= (width * height);
            b /= (width * height);

            // 和目标色按比例混合
            r = (int) (r * adaptiveColorRate + (((color >> 16) & 0xFF) * (1.00 - adaptiveColorRate)));
            g = (int) (g * adaptiveColorRate + (((color >> 8) & 0xFF) * (1.00 - adaptiveColorRate)));
            b = (int) (b * adaptiveColorRate + ((color & 0xFF) * (1.00 - adaptiveColorRate)));

            newColor = (r << 16) | (g << 8) | b;
        } else {
            newColor = color;
        }

        // 绘制区域
        for (var posX = width + x - 1; posX >= x; posX--) {
            for (var posY = height + y - 1; posY >= y; posY--) {
                image.setRGB(posX, posY, newColor);
            }
        }
    }
}
