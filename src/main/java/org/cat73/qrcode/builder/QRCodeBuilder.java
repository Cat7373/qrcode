package org.cat73.qrcode.builder;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Setter;
import org.cat73.qrcode.QRCode;
import org.cat73.qrcode.style.IQRCodeStyle;
import org.cat73.qrcode.util.Lang;
import org.cat73.qrcode.util.Strings;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码 Builder
 */
public class QRCodeBuilder {
    // **** 生成参数 ****
    /**
     * 内容
     */
    private final String content;
    /**
     * 容错级别，默认为 M(15%)
     */
    @Setter(AccessLevel.PRIVATE)
    private ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.M;
    /**
     * QRVersion，用于控制二维码的大小，范围 1 ～ 40，如值在范围外，则使用自适应
     */
    private int qrVersion = -1;
    /**
     * 内容编码
     */
    private Charset charset = StandardCharsets.UTF_8;
    /**
     * 边框块数
     */
    private int borderBlock = 1;
    // **** 图片参数 ****
    /**
     * 块大小(像素)
     */
    private int blockSize = 8;
    /**
     * 前景色(RGB)
     */
    private int foregroundColor = 0x00000000;
    /**
     * 背景色(RGB)
     */
    private int backgroundColor = 0x00FFFFFF;
    /**
     * 输出时用的样式<br>
     * 非默认样式时，建议 {@link #qrVersion} 至少为 2
     */
    private IQRCodeStyle style = QRCode.STYLE_DEFAULT;
    // **** Logo ****
    /**
     * Logo 图片
     */
    private BufferedImage logoImg = null;
    /**
     * Logo 图片大小(块数)
     */
    private int logoSize = 0;
    // TODO **** 背景图 ****
    // **** toStr 参数 ****
    /**
     * toStr 时，前景色输出为的内容
     */
    private String foregroundStr = "  ";
    /**
     * toStr 时，背景色输出为的内容
     */
    private String backgroundStr = "██";

    // **** 生成参数 ****
    /**
     * 基于指定内容构建一个二维码 Builder
     * @param content 内容
     */
    public QRCodeBuilder(@NonNull String content) {
        if (Strings.isEmpty(content)) throw Lang.makeThrow("content is empty.");
        this.content = content;
    }

    /**
     * 使用 low 纠错级别(7%)
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder lowErrorCorrection() {
        this.errorCorrectionLevel = ErrorCorrectionLevel.L;
        return this;
    }

    /**
     * 使用 medium 纠错级别(15%)
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder mediumErrorCorrection() {
        this.errorCorrectionLevel = ErrorCorrectionLevel.M;
        return this;
    }

    /**
     * 使用 quartile 纠错级别(25%)
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder quartileErrorCorrection() {
        this.errorCorrectionLevel = ErrorCorrectionLevel.Q;
        return this;
    }

    /**
     * 使用 high 纠错级别(30%)
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder highErrorCorrection() {
        this.errorCorrectionLevel = ErrorCorrectionLevel.H;
        return this;
    }

    /**
     * 设置 QRVersion，用于控制二维码的大小，范围 1 ～ 40，如需要自适应，请设置为 0
     * @param version QRVersion 的值
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder qrVersion(int version) {
        if (version < 0 || version > 40) throw new IndexOutOfBoundsException("qrVersion");
        this.qrVersion = version;
        return this;
    }

    /**
     * 设置内容的编码
     * @param charset 内容的编码
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder charset(@NonNull Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 设置边框块数
     * @param borderBlock 边框块数
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder borderBlock(int borderBlock) {
        if (borderBlock < 0) throw Lang.makeThrow("borderBlock < 0");
        this.borderBlock = borderBlock;
        return this;
    }

    // **** 图片参数 ****
    /**
     * 设置块大小(像素)
     * @param blockSize 块大小(像素)
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder blockSize(int blockSize) {
        if (blockSize < 1) throw Lang.makeThrow("blockSize < 1");
        this.blockSize = blockSize;
        return this;
    }

    /**
     * 设置前景色
     * @param r 红色的量(0 ~ 255)
     * @param g 绿色的量(0 ~ 255)
     * @param b 蓝色的量(0 ~ 255)
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder foregroundColor(int r, int g, int b) {
        this.foregroundColor = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
        return this;
    }

    /**
     * 设置背景色
     * @param r 红色的量(0 ~ 255)
     * @param g 绿色的量(0 ~ 255)
     * @param b 蓝色的量(0 ~ 255)
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder backgroundColor(int r, int g, int b) {
        this.backgroundColor = ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
        return this;
    }

    /**
     * 设置输出时用的样式<br>
     * 非默认样式时，建议 {@link #qrVersion(int)} 至少为 2，如识别率较低，则推荐使用 {@link #highErrorCorrection()}
     *@param style 输出时用的样式
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder style(@NonNull IQRCodeStyle style) {
        this.style = style;
        return this;
    }

    // **** Logo ****
    /**
     * 设置为无 Logo 图片
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder noneLogo() {
        this.logoImg = null;
        this.logoSize = 0;
        return this;
    }

    /**
     * 设置 Logo 图片
     * @param img 图片
     * @param size 图片大小(块)，如果希望图片不会出现覆盖半个块的情况，则应该使用奇数
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder logo(@NonNull BufferedImage img, int size) {
        if (size < 1) throw Lang.makeThrow("size < 1");
        this.logoImg = img;
        this.logoSize = size;
        return this;
    }

    /**
     * 设置 Logo 图片
     * @param in 图片的输入流
     * @param size 图片大小(块)，如果希望图片不会出现覆盖半个块的情况，则应该使用奇数
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder logo(@NonNull InputStream in, int size) {
        return Lang.wrapCode(() -> this.logo(ImageIO.read(in), size));
    }

    /**
     * 设置 Logo 图片
     * @param file 图片的文件
     * @param size 图片大小(块)，如果希望图片不会出现覆盖半个块的情况，则应该使用奇数
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder logo(@NonNull File file, int size) {
        return Lang.wrapCode(() -> this.logo(new FileInputStream(file), size));
    }

    /**
     * 设置 Logo 图片
     * @param filePath 图片的文件路径
     * @param size 图片大小(块)，如果希望图片不会出现覆盖半个块的情况，则应该使用奇数
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder logo(@NonNull String filePath, int size) {
        return this.logo(new File(filePath), size);
    }

    /**
     * 设置 Logo 图片
     * @param path 图片在 ClassPath 中的路径
     * @param size 图片大小(块)，如果希望图片不会出现覆盖半个块的情况，则应该使用奇数
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder classpathLogo(@NonNull String path, int size) {
        return this.logo(QRCodeBuilder.class.getResourceAsStream(path), size);
    }

    // **** toStr 参数 ****
    /**
     * 设置 toStr 时，前景色输出为的内容
     * @param foregroundStr 内容
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder foregroundStr(@NonNull String foregroundStr) {
        if (Strings.isEmpty(foregroundStr)) throw Lang.makeThrow("foregroundStr is empty.");
        this.foregroundStr = foregroundStr;
        return this;
    }

    /**
     * 设置 toStr 时，背景色输出为的内容
     * @param backgroundStr 内容
     * @return 自身实例，方便链式调用
     */
    public QRCodeBuilder backgroundStr(@NonNull String backgroundStr) {
        if (Strings.isEmpty(backgroundStr)) throw Lang.makeThrow("backgroundStr is empty.");
        this.backgroundStr = backgroundStr;
        return this;
    }

    /**
     * 输出为 QRCode
     * @return QRCode 实例
     */
    private com.google.zxing.qrcode.encoder.QRCode toQrcode() {
        // 自定义参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, this.errorCorrectionLevel);
        // 内容编码
        hints.put(EncodeHintType.CHARACTER_SET, this.charset.name());
        if (this.qrVersion >= 1 && this.qrVersion <= 40) {
            hints.put(EncodeHintType.QR_VERSION, this.qrVersion);
        }

        return Lang.wrapCode(() -> Encoder.encode(this.content, this.errorCorrectionLevel, hints));
    }

    /**
     * 输出为 BitMatrix
     * @return BitMatrix 实例
     */
    private ByteMatrix toBitMatrix() {
        return this.toQrcode().getMatrix();
    }

    /**
     * 输出为填充数组
     * @return 数组，一维为横坐标，二维为纵坐标，值为 true 时应当被填充为前景色，false 时应当被填充为背景色
     */
    public boolean[][] toArray() {
        // 获取 BitMatrix
        ByteMatrix matrix = this.toBitMatrix();

        // 宽高
        int width = matrix.getWidth();

        // 生成结果并返回
        boolean[][] result = new boolean[width + this.borderBlock * 2][width + this.borderBlock * 2];
        for (int y = 0, posY = this.borderBlock; y < width; y++, posY++) {
            for (int x = 0, posX = this.borderBlock; x < width; x++, posX++) {
                result[posY][posX] = (matrix.get(x, y) == 1);
            }
        }
        return result;
    }

    /**
     * 输出为字符串，通常用于打印到日志中
     * @return 字符串
     */
    public String toStr() {
        // 获取填充数组
        boolean[][] arr = this.toArray();

        // 生成结果并返回
        int width = arr.length;
        StringBuilder sb = new StringBuilder();
        for (boolean[] xarr : arr) {
            for (int x = 0; x < width; x++) {
                sb.append(xarr[x] ? this.backgroundStr : this.foregroundStr);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * 输出为图片
     * @return 图片
     */
    public BufferedImage toImg() {
        // 生成图片
        boolean[][] arr = this.toArray();
        BufferedImage img = this.style.toImg(arr, this.borderBlock, this.blockSize, this.foregroundColor, this.backgroundColor);

        // 如果有 Logo 则画 Logo
        if (this.logoImg != null) {
            Graphics2D gs = img.createGraphics();

            // 计算输出位置
            int startXPos = (int) (((arr.length / 2.0) - (this.logoSize / 2.0)) * this.blockSize);
            int startYPos = (int) (((arr.length / 2.0) - (this.logoSize / 2.0)) * this.blockSize);
            int width = this.logoSize * this.blockSize;
            int height = this.logoSize * this.blockSize;

            // 绘制 Logo 图片
            gs.drawImage(this.logoImg, startXPos, startYPos, width, height, null);

            // 销毁 Graphics，释放资源
            // 如果上面的代码出现异常，会导致这行代码无法被调用到
            // 但 JVM 最终仍会执行这个方法同样的流程去回收资源，因此无需做特殊处理
            gs.dispose();
        }

        // 返回结果
        return img;
    }

    /**
     * 输出为 BMP 图片
     * @return BMP 图片数据的输出流
     */
    public InputStream toBmp() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(this.toImg(), "BMP", out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 输出为 JPG 图片
     * @param quality 压缩质量，范围在 0.0 &lt; quality &lt; 1.0 之间，超出范围则不使用压缩
     * @return JPG 图片数据的输出流
     */
    public InputStream toJpg(float quality) {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("JPEG").next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        if (quality > 0.0f && quality < 1.0f) {
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(quality);
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writer.setOutput(ImageIO.createImageOutputStream(out));
            writer.write(null, new IIOImage(this.toImg() ,null,null), iwp);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        } finally {
            writer.dispose();
        }
    }

    /**
     * 输出为 PNG 图片
     * @return PNG 图片数据的输出流
     */
    public InputStream toPng() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(this.toImg(), "PNG", out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
    }

    /**
     * 写出 BMP 图片到文件中
     * @param file 目标文件
     */
    public void writeBmpTo(@NonNull File file) {
        Lang.wrapCode(() -> ImageIO.write(this.toImg(), "BMP", file));
    }

    /**
     * 写出 JPG 图片到文件中
     * @param file 目标文件
     * @param quality 压缩质量，范围在 0.0 &lt; quality &lt; 1.0 之间，超出范围则不使用压缩
     */
    public void writeJpgTo(@NonNull File file, float quality) {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("JPEG").next();
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        if (quality > 0.0f && quality < 1.0f) {
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(quality);
        }

        try {
            writer.setOutput(ImageIO.createImageOutputStream(file));
            writer.write(null, new IIOImage(this.toImg() ,null,null), iwp);
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        } finally {
            writer.dispose();
        }
    }

    /**
     * 写出 PNG 图片到文件中
     * @param file 目标文件
     */
    public void writePngTo(@NonNull File file) {
        Lang.wrapCode(() -> ImageIO.write(this.toImg(), "PNG", file));
    }

    /**
     * 写出 BMP 图片到文件中
     * @param path 目标文件的路径
     */
    public void writeBmpTo(@NonNull String path) {
        this.writeBmpTo(new File(path));
    }

    /**
     * 写出 JPG 图片到文件中
     * @param path 目标文件的路径
     * @param quality 压缩质量，范围在 0.0 &lt; quality &lt; 1.0 之间，超出范围则不使用压缩
     */
    public void writeJpgTo(@NonNull String path, float quality) {
        this.writeJpgTo(new File(path), quality);
    }

    /**
     * 写出 PNG 图片到文件中
     * @param path 目标文件的路径
     */
    public void writePngTo(@NonNull String path) {
        this.writePngTo(new File(path));
    }
}
