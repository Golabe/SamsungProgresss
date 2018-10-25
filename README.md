# SamsungProgress
## 自定义进度条 仿三星健康，不是很像
![markdown](https://github.com/Golabe/SamsungProgresss/blob/master/images/image1.png?raw=true "markdown")
![markdown](https://github.com/Golabe/SamsungProgresss/blob/master/images/image2.png?raw=true "markdown")
![markdown](https://github.com/Golabe/SamsungProgresss/blob/master/images/image3.png?raw=true "markdown")
![markdown](https://github.com/Golabe/SamsungProgresss/blob/master/images/image4.png?raw=true "markdown")
## In layout.xml


    <top.golabe.library.SamsungProgressView
        android:id="@+id/progress"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:samsung_border="6dp"
        app:samsung_duration="400"
        app:samsung_max="120"
        app:samsung_min="-50"
        app:samsung_progress="50"

        />

## In java
              mSamsungProgressView.setBgColor();
              mSamsungProgressView.setBorder();
              mSamsungProgressView.setDuration();
              mSamsungProgressView.setMax();
              mSamsungProgressView.setMin();
              mSamsungProgressView.setSuffix();
              mSamsungProgressView.setTextColor();
              mSamsungProgressView.setTextSize();
## Attrs

            <attr name="samsung_bg_color" format="color" />
            <attr name="samsung_progress" format="integer" />
            <attr name="samsung_min" format="integer" />
            <attr name="samsung_max" format="integer" />
            <attr name="samsung_progress_bg_color" format="color" />
            <attr name="samsung_progress_color" format="color" />
            <attr name="samsung_border" format="dimension" />
            <attr name="samsung_text_color" format="color" />
            <attr name="samsung_text_size" format="dimension" />
            <attr name="samsung_suffix" format="string"/>
            <attr name="samsung_duration" format="integer"/>


