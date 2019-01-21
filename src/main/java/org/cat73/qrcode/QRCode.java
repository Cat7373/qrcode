package org.cat73.qrcode;

import lombok.NonNull;
import org.cat73.qrcode.builder.QRCodeBuilder;
import org.cat73.qrcode.style.DefaultQRCodeStyle;
import org.cat73.qrcode.style.IQRCodeStyle;
import org.cat73.qrcode.style.ImgBlockQRCodeStyle;
import org.cat73.qrcode.style.ImgQRCodeStyle;

/**
 * 二维码工具类
 */
public class QRCode {
    protected QRCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * 普通纯色样式<br>
     */
    public static final IQRCodeStyle STYLE_DEFAULT = new DefaultQRCodeStyle();

    /**
     * 内置的二维码样式 - 1<br>
     */
    public static final IQRCodeStyle STYLE_01 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/01/11.png")
            .classpathImg(1, 1, "/imgs/01/12.png")
            .classpathImg(1, 1, "/imgs/01/13.png")
            .classpathImg(2, 1, "/imgs/01/21.png")
            .classpathImg(2, 1, "/imgs/01/22.png")
            .classpathImg(2, 1, "/imgs/01/23.png")
            .classpathImg(1, 2, "/imgs/01/31.png")
            .classpathImg(1, 2, "/imgs/01/32.png")
            .classpathImg(1, 2, "/imgs/01/33.png")
            .classpathImg(2, 2, "/imgs/01/41.png")
            .classpathImg(2, 2, "/imgs/01/42.png")
            .classpathImg(2, 2, "/imgs/01/43.png")
            .classpathEye("/imgs/01/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 2<br>
     * <em>识别率较低</em><br>
     */
    public static final IQRCodeStyle STYLE_02 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/02/11.png")
            .classpathImg(1, 1, "/imgs/02/12.png")
            .classpathImg(1, 1, "/imgs/02/13.png")
            .classpathImg(2, 1, "/imgs/02/21.png")
            .classpathImg(2, 1, "/imgs/02/22.png")
            .classpathImg(2, 1, "/imgs/02/23.png")
            .classpathImg(1, 2, "/imgs/02/31.png")
            .classpathImg(1, 2, "/imgs/02/32.png")
            .classpathImg(1, 2, "/imgs/02/33.png")
            .classpathImg(2, 2, "/imgs/02/41.png")
            .classpathImg(2, 2, "/imgs/02/42.png")
            .classpathImg(2, 2, "/imgs/02/43.png")
            .classpathEye("/imgs/02/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 3<br>
     */
    public static final IQRCodeStyle STYLE_03 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/03/11.png")
            .classpathImg(1, 1, "/imgs/03/12.png")
            .classpathImg(1, 1, "/imgs/03/13.png")
            .classpathImg(2, 1, "/imgs/03/21.png")
            .classpathImg(2, 1, "/imgs/03/22.png")
            .classpathImg(2, 1, "/imgs/03/23.png")
            .classpathImg(1, 2, "/imgs/03/31.png")
            .classpathImg(1, 2, "/imgs/03/32.png")
            .classpathImg(1, 2, "/imgs/03/33.png")
            .classpathImg(2, 2, "/imgs/03/41.png")
            .classpathImg(2, 2, "/imgs/03/42.png")
            .classpathImg(2, 2, "/imgs/03/43.png")
            .classpathEye("/imgs/03/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 4<br>
     */
    public static final IQRCodeStyle STYLE_04 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/04/11.png")
            .classpathImg(1, 1, "/imgs/04/12.png")
            .classpathImg(1, 1, "/imgs/04/13.png")
            .classpathImg(2, 1, "/imgs/04/21.png")
            .classpathImg(2, 1, "/imgs/04/22.png")
            .classpathImg(2, 1, "/imgs/04/23.png")
            .classpathImg(1, 2, "/imgs/04/31.png")
            .classpathImg(1, 2, "/imgs/04/32.png")
            .classpathImg(1, 2, "/imgs/04/33.png")
            .classpathImg(2, 2, "/imgs/04/41.png")
            .classpathImg(2, 2, "/imgs/04/42.png")
            .classpathImg(2, 2, "/imgs/04/43.png")
            .classpathEye("/imgs/04/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 5<br>
     */
    public static final IQRCodeStyle STYLE_05 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/05/11.png")
            .classpathImg(1, 1, "/imgs/05/12.png")
            .classpathImg(1, 1, "/imgs/05/13.png")
            .classpathImg(2, 1, "/imgs/05/21.png")
            .classpathImg(2, 1, "/imgs/05/22.png")
            .classpathImg(2, 1, "/imgs/05/23.png")
            .classpathImg(1, 2, "/imgs/05/31.png")
            .classpathImg(1, 2, "/imgs/05/32.png")
            .classpathImg(1, 2, "/imgs/05/33.png")
            .classpathImg(2, 2, "/imgs/05/41.png")
            .classpathImg(2, 2, "/imgs/05/42.png")
            .classpathImg(2, 2, "/imgs/05/43.png")
            .classpathEye("/imgs/05/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 6<br>
     */
    public static final IQRCodeStyle STYLE_06 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/06/11.png")
            .classpathImg(1, 1, "/imgs/06/12.png")
            .classpathImg(1, 1, "/imgs/06/13.png")
            .classpathImg(2, 1, "/imgs/06/21.png")
            .classpathImg(2, 1, "/imgs/06/22.png")
            .classpathImg(2, 1, "/imgs/06/23.png")
            .classpathImg(1, 2, "/imgs/06/31.png")
            .classpathImg(1, 2, "/imgs/06/32.png")
            .classpathImg(1, 2, "/imgs/06/33.png")
            .classpathImg(2, 2, "/imgs/06/41.png")
            .classpathImg(2, 2, "/imgs/06/42.png")
            .classpathImg(2, 2, "/imgs/06/43.png")
            .classpathEye("/imgs/06/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 7<br>
     */
    public static final IQRCodeStyle STYLE_07 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/07/1_1_01.png")
            .classpathImg(1, 2, "/imgs/07/1_2_01.png")
            .classpathImg(1, 2, "/imgs/07/1_2_02.png")
            .classpathImg(1, 4, "/imgs/07/1_4_01.png")
            .classpathImg(2, 2, "/imgs/07/2_2_01.png")
            .classpathImg(2, 2, "/imgs/07/2_2_02.png")
            .classpathImg(2, 2, "/imgs/07/2_2_03.png")
            .classpathImg(2, 3, "/imgs/07/2_3_01.png")
            .classpathImg(3, 1, "/imgs/07/3_1_01.png")
            .classpathImg(3, 2, "/imgs/07/3_2_01.png")
            .classpathEye("/imgs/07/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 8<br>
     */
    public static final IQRCodeStyle STYLE_08 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/08/1_1_01.png")
            .classpathImg(1, 3, "/imgs/08/1_3_01.png")
            .classpathImg(1, 4, "/imgs/08/1_4_01.png")
            .classpathImg(2, 1, "/imgs/08/2_1_01.png")
            .classpathImg(2, 2, "/imgs/08/2_2_01.png")
            .classpathImg(2, 2, "/imgs/08/2_2_02.png")
            .classpathEye("/imgs/08/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 9<br>
     */
    public static final IQRCodeStyle STYLE_09 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/09/1_1_01.png")
            .classpathImg(1, 3, "/imgs/09/1_3_01.png")
            .classpathImg(1, 4, "/imgs/09/1_4_01.png")
            .classpathImg(2, 1, "/imgs/09/2_1_01.png")
            .classpathImg(2, 2, "/imgs/09/2_2_01.png")
            .classpathImg(2, 2, "/imgs/09/2_2_02.png")
            .classpathEye("/imgs/09/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 10<br>
     */
    public static final IQRCodeStyle STYLE_10 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/10/1_1_01.png")
            .classpathImg(1, 3, "/imgs/10/1_3_01.png")
            .classpathImg(1, 4, "/imgs/10/1_4_01.png")
            .classpathImg(2, 1, "/imgs/10/2_1_01.png")
            .classpathImg(2, 2, "/imgs/10/2_2_01.png")
            .classpathImg(2, 2, "/imgs/10/2_2_02.png")
            .classpathImg(2, 3, "/imgs/10/2_3_01.png")
            .classpathImg(3, 2, "/imgs/10/3_2_01.png")
            .classpathEye("/imgs/10/eye.png")
            .build();

    /**
     * 内置的二维码样式 - 11<br>
     */
    public static final IQRCodeStyle STYLE_11 = QRCode.imgBlockStyleBuilder()
            .classpathImg(1, 1, "/imgs/11/11.png")
            .classpathImg(1, 1, "/imgs/11/12.png")
            .classpathImg(1, 1, "/imgs/11/13.png")
            .classpathImg(1, 2, "/imgs/11/21.png")
            .classpathImg(1, 2, "/imgs/11/22.png")
            .classpathImg(1, 2, "/imgs/11/23.png")
            .classpathImg(2, 1, "/imgs/11/31.png")
            .classpathImg(2, 1, "/imgs/11/32.png")
            .classpathImg(2, 1, "/imgs/11/33.png")
            .classpathImg(2, 2, "/imgs/11/41.png")
            .classpathImg(2, 2, "/imgs/11/42.png")
            .classpathImg(2, 2, "/imgs/11/43.png")
            .classpathEye("/imgs/11/eye.png")
            .build();

    /**
     * 内置的所有二维码样式合并而成的样式<br>
     * <em>识别率较低</em><br>
     */
    public static final IQRCodeStyle STYLE_ALL = ImgBlockQRCodeStyle.merge(
            (ImgBlockQRCodeStyle) QRCode.STYLE_01,
            (ImgBlockQRCodeStyle) QRCode.STYLE_02,
            (ImgBlockQRCodeStyle) QRCode.STYLE_03,
            (ImgBlockQRCodeStyle) QRCode.STYLE_04,
            (ImgBlockQRCodeStyle) QRCode.STYLE_05,
            (ImgBlockQRCodeStyle) QRCode.STYLE_06,
            (ImgBlockQRCodeStyle) QRCode.STYLE_07,
            (ImgBlockQRCodeStyle) QRCode.STYLE_08,
            (ImgBlockQRCodeStyle) QRCode.STYLE_09,
            (ImgBlockQRCodeStyle) QRCode.STYLE_10,
            (ImgBlockQRCodeStyle) QRCode.STYLE_11
    );

    /**
     * 生成一个二维码的 Builder
     * @param content 二维码的内容
     * @return 二维码 Builder 的实例
     */
    public static QRCodeBuilder content(@NonNull String content) {
        return new QRCodeBuilder(content);
    }

    /**
     * 获取一个用图片填充 block 的二维码样式的 Builder
     * @return 用图片填充 block 的二维码样式的 Builder 的实例
     */
    public static ImgBlockQRCodeStyle.ImgBlockQRCodeStyleBuilder imgBlockStyleBuilder() {
        return ImgBlockQRCodeStyle.builder();
    }

    /**
     * 获取一个将特定图片改造为二维码的样式的 Builder
     * @return 将特定图片改造为二维码的样式的 Builder
     */
    public static ImgQRCodeStyle.ImgQRCodeStyleBuilder imgStyleBuilder() {
        return ImgQRCodeStyle.builder();
    }

    // TODO 解析二维码
    // TODO 输出支持透明色
}
