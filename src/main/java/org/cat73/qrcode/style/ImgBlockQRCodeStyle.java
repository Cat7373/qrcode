package org.cat73.qrcode.style;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.cat73.qrcode.util.Lang;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.*;

/**
 * 用图片填充 block 的二维码样式
 */
// TODO 支持非矩形图片(如圆形)
public final class ImgBlockQRCodeStyle implements IQRCodeStyle {
    /**
     * 随机数生成器
     */
    private static final Random rand = new Random();

    /**
     * 用于替换的图片
     */
    @RequiredArgsConstructor
    private static class Imgs {
        /**
         * 图片占用的 block 宽度
         */
        private final Integer width;
        /**
         * 图片占用的 block 高度
         */
        private final Integer height;
        /**
         * 图片列表
         */
        private final List<BufferedImage> imgs;
    }

    /**
     * 用于替换的图片列表，实际替换时会从第一个开始，依次判断宽高是否合适
     */
    private final List<Imgs> imgs;
    /**
     * 码眼图片列表
     */
    private final List<BufferedImage> eyeImgs;

    /**
     * 构造一个二维码样式的实例
     * @param imgs 用于替换的图片列表，实际替换时会从第一个开始，依次判断宽高是否合适
     * @param eyeImgs 码眼图片列表
     */
    private ImgBlockQRCodeStyle(@NonNull List<Imgs> imgs, @NonNull List<BufferedImage> eyeImgs) {
        this.imgs = imgs;
        this.eyeImgs = eyeImgs;
    }

    /**
     * 获取一个用图片填充 block 的二维码样式的 Builder
     * @return 用图片填充 block 的二维码样式的 Builder 的实例
     */
    public static ImgBlockQRCodeStyleBuilder builder() {
        return new ImgBlockQRCodeStyleBuilder();
    }

    /**
     * 合并多个二维码样式
     * @param styles 被合并的样式列表
     * @return 合并后的样式
     */
    public static ImgBlockQRCodeStyle merge(ImgBlockQRCodeStyle... styles) {
        var builder = ImgBlockQRCodeStyle.builder();
        for (ImgBlockQRCodeStyle style : styles) {
            style.eyeImgs.forEach(builder::eye);
            style.imgs.forEach(imgs -> imgs.imgs.forEach(img -> builder.img(imgs.width, imgs.height, img)));
        }

        return builder.build();
    }

    /**
     * 输出为图片
     * @param arr 填充数组
     * @param borderBlock 边框宽度(方块数)
     * @param blockSize 块大小(像素)
     * @param backgroundColor 背景色
     * @return 图片
     */
    @Override
    public BufferedImage toImg(@NonNull boolean[][] arr, int borderBlock, int blockSize, int foregroundColor, int backgroundColor) {
        // 宽高
        int blockWidth = arr.length;
        int imgWidth = blockWidth * blockSize;

        // 结果图片
        BufferedImage image = new BufferedImage(imgWidth, imgWidth, BufferedImage.TYPE_INT_RGB);
        // 获取画笔
        Graphics2D gs = image.createGraphics();
        gs.setBackground(new Color(backgroundColor));
        gs.clearRect(0, 0, imgWidth, imgWidth);

        // 画三个码眼
        gs.drawImage(this.eyeImgs.get(rand.nextInt(this.eyeImgs.size())), borderBlock * blockSize, borderBlock * blockSize, blockSize * 7, blockSize * 7, null);
        gs.drawImage(this.eyeImgs.get(rand.nextInt(this.eyeImgs.size())), (blockWidth - borderBlock - 7) * blockSize, borderBlock * blockSize, blockSize * 7, blockSize * 7, null);
        gs.drawImage(this.eyeImgs.get(rand.nextInt(this.eyeImgs.size())), borderBlock * blockSize, (blockWidth - borderBlock - 7) * blockSize, blockSize * 7, blockSize * 7, null);
        // 将三个码眼的位置设置为 false
        // 左上
        for (var x = borderBlock + 6; x >= borderBlock; x--) {
            for (var y = borderBlock + 6; y >= borderBlock; y--) {
                arr[x][y] = false;
            }
        }
        // 左下、右上
        for (var a = blockWidth - borderBlock - 7; a < blockWidth; a++) { // 横轴或纵轴数字较大的那边的坐标
            for (var b = borderBlock + 6; b >= borderBlock; b--) { // 与外循环的轴垂直的轴的坐标
                arr[a][b] = false; // 左下
                arr[b][a] = false; // 右上
            }
        }

        // 填充内容
        for (var y = borderBlock; y < blockWidth; y++) {
            for (var x = borderBlock; x < blockWidth; x++) {
                if (!arr[y][x]) continue; // 不需要填充的点直接过

                // 依次判断能否填充
                for (var imgs : this.imgs) {
                    if (this.canDraw(imgs.width, imgs.height, x, y, arr)) {
                        var startX = x * blockSize;
                        var startY = y * blockSize;

                        // 绘制图片
                        var img = imgs.imgs.get(rand.nextInt(imgs.imgs.size()));
                        gs.drawImage(img, startX, startY, imgs.width * blockSize, imgs.height * blockSize, null);

                        // 跳出循环，不再继续尝试
                        break;
                    }
                }
            }
        }

        // 销毁 Graphics，释放资源
        // 如果上面的代码出现异常，会导致这行代码无法被调用到
        // 但 JVM 最终仍会执行这个方法同样的流程去回收资源，因此无需做特殊处理
        gs.dispose();

        // 返回结果
        return image;
    }

    /**
     * 判断能否进行填充，如可以，则将填充数组的被填充部分设置为 false
     * @param width 填充的宽度
     * @param height 填充的高度
     * @param x 开始位置的横坐标
     * @param y 开始位置的纵坐标
     * @param arr 填充数组
     * @return 能否进行填充
     */
    private boolean canDraw(int width, int height, int x, int y, boolean[][] arr) {
        if ((x + width > arr.length) || (y + height > arr.length)) {
            return false;
        }

        for (var yy = y; yy < height + y; yy++) {
            for (var xx = x; xx < width + x; xx++) {
                if (!arr[yy][xx]) return false;
            }
        }
        for (var yy = y; yy < height + y; yy++) {
            for (var xx = x; xx < width + x; xx++) {
                arr[yy][xx] = false;
            }
        }
        return true;
    }

    /**
     * 二维码样式的 Builder
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ImgBlockQRCodeStyleBuilder {
        /**
         * 用于替换的图片列表
         */
        private Map<Integer, Map<Integer, List<BufferedImage>>> imgs = new HashMap<>();
        /**
         * 码眼图片列表
         */
        private List<BufferedImage> eyeImgs = new ArrayList<>();

        /**
         * 添加一张用于替换的图片
         * @param width 图片的宽度(方块数)
         * @param height 图片的高度(方块数)
         * @param img 图片
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder img(int width, int height, @NonNull BufferedImage img) {
            this.imgs.computeIfAbsent(width, k -> new HashMap<>())
                    .computeIfAbsent(height, k -> new ArrayList<>())
                    .add(img);
            return this;
        }

        /**
         * 添加一张用于替换的图片
         * @param width 图片的宽度(方块数)
         * @param height 图片的高度(方块数)
         * @param in 图片的输入流
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder img(int width, int height, @NonNull InputStream in) {
            return Lang.wrapCode(() -> this.img(width, height, ImageIO.read(in)));
        }

        /**
         * 添加一张用于替换的图片
         * @param width 图片的宽度(方块数)
         * @param height 图片的高度(方块数)
         * @param file 图片的文件
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder img(int width, int height, @NonNull File file) {
            return Lang.wrapCode(() -> this.img(width, height, new FileInputStream(file)));
        }

        /**
         * 添加一张用于替换的图片
         * @param width 图片的宽度(方块数)
         * @param height 图片的高度(方块数)
         * @param filePath 图片的文件路径
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder img(int width, int height, @NonNull String filePath) {
            return this.img(width, height, new File(filePath));
        }

        /**
         * 从 ClassPath 添加一张用于替换的图片
         * @param width 图片的宽度(方块数)
         * @param height 图片的高度(方块数)
         * @param path 图片在 ClassPath 中的路径
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder classpathImg(int width, int height, @NonNull String path) {
            return this.img(width, height, ImgBlockQRCodeStyle.class.getResourceAsStream(path));
        }

        /**
         * 添加一张码眼图片
         * @param img 图片
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder eye(@NonNull BufferedImage img) {
            this.eyeImgs.add(img);
            return this;
        }

        /**
         * 添加一张码眼图片
         * @param in 图片的输入流
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder eye(@NonNull InputStream in) {
            return Lang.wrapCode(() -> this.eye(ImageIO.read(in)));
        }

        /**
         * 添加一张码眼图片
         * @param file 图片的文件
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder eye(@NonNull File file) {
            return Lang.wrapCode(() -> this.eye(new FileInputStream(file)));
        }

        /**
         * 添加一张码眼图片
         * @param filePath 图片的文件路径
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder eye(@NonNull String filePath) {
            return this.eye(new File(filePath));
        }

        /**
         * 从 ClassPath 添加一张码眼图片
         * @param path 图片在 ClassPath 中的路径
         * @return 自身实例，方便链式调用
         */
        public ImgBlockQRCodeStyleBuilder classpathEye(@NonNull String path) {
            return this.eye(ImgBlockQRCodeStyle.class.getResourceAsStream(path));
        }

        /**
         * 构建二维码样式的实例
         * @return 构建结果
         */
        public ImgBlockQRCodeStyle build() {
            // 校验至少包含一张 1 * 1 个方块的图片
            if (Optional.of(this.imgs).map(m -> m.get(1)).map(m -> m.get(1)).isEmpty()) {
                throw Lang.makeThrow("QRStyle 中必须包含至少一张 1 * 1 的图片");
            }
            if (this.eyeImgs.isEmpty()) {
                throw Lang.makeThrow("QRStyle 中必须包含至少一张码眼图片");
            }

            // 转换为按图片面积排序的列表
            List<Imgs> list = new ArrayList<>();
            this.imgs.forEach((width, m) -> m.forEach((height, img) -> list.add(new Imgs(width, height, img))));
            list.sort((a, b) -> (b.width * b.height) - (a.width * a.height));

            // 构建并返回实例
            return new ImgBlockQRCodeStyle(list, this.eyeImgs);
        }
    }
}
