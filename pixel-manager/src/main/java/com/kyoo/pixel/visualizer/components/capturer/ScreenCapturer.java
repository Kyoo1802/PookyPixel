package com.kyoo.pixel.visualizer.components.capturer;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.kyoo.pixel.visualizer.data.ImageFrame;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameUtils;

@Log4j2
@Singleton
public final class ScreenCapturer implements Capturer {

  private Grabber grabber;
  private Rectangle captureWindow;
  private FrameStats frameStats;

  @Inject
  public ScreenCapturer(Provider<FrameStats> frameStats,
      @Named("capturer.frame.width") int frameWidth,
      @Named("capturer.frame.height") int frameHeight,
      @Named("capturer.image.width") int imageWidth,
      @Named("capturer.image.height") int imageHeight) {
    this.captureWindow = new Rectangle(0, 0, imageWidth, imageHeight);
    this.grabber = new Grabber();
    this.grabber.startGrabber(frameWidth, frameHeight);
    this.frameStats = frameStats.get();
  }

  private static BufferedImage resize(Rectangle captureWindow, BufferedImage image) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double widthRatio = (double) image.getWidth() / screenSize.width;
    double heightRatio = (double) image.getHeight() / screenSize.height;
    int x = (int) (
        Math.min(Math.max(captureWindow.getX(), 0),
            screenSize.width - (int) captureWindow.getWidth())
            * widthRatio);
    int y = (int) (
        Math.min(Math.max(captureWindow.getY(), 0),
            screenSize.height - (int) captureWindow.getHeight())
            * heightRatio);
    int width = (int) (captureWindow.getWidth() * widthRatio);
    int height = (int) (captureWindow.getHeight() * heightRatio);
    return image.getSubimage(x, y, width, height);
  }

  @Override
  public Optional<ImageFrame> getImageFrame() {
    Optional<BufferedImage> image = grabber.capture(captureWindow);
    if (image.isEmpty()) {
      return Optional.empty();
    }
    frameStats.captureStats();
    ImageFrame imageFrame = new ImageFrame();
    imageFrame.setBufferedImage(image.get());
    imageFrame.setFrameNumber(frameStats.getFrameNumber());
    imageFrame.setFrameRate(frameStats.getFrameRate());
    return Optional.of(imageFrame);
  }

  @Override
  public void stop() {
    grabber.stopGrabber();
  }

  public void updateCaptureWindow(Rectangle captureWindow) {
    this.captureWindow = captureWindow;
  }

  final class Grabber {

    private FFmpegFrameGrabber grabber;
    private boolean active;

    protected void startGrabber(int frameWidth, int frameHeight) {
      log.debug("Starting Grabber");
      if (active) {
        log.debug("Grabber already started");
        return;
      }
      try {
        if (grabber == null) {
          grabber = new FFmpegFrameGrabber("Capture screen 0");
          grabber.setFormat("avfoundation");
          grabber.setImageWidth(frameWidth);
          grabber.setImageHeight(frameHeight);
        }
        grabber.start();
        log.info("Grabber started");
      } catch (Exception e) {
        log.error("Grabber exception while starting: " + e.getMessage());
      }
      active = true;
    }

    protected void stopGrabber() {
      try {
        if (grabber != null && active) {
          grabber.stop();
        }
        active = false;
      } catch (Exception e) {
        log.error("Grabber Exception while stopping: " + e.getMessage());
      }
    }

    protected Optional<BufferedImage> capture(Rectangle captureWindow) {
      Preconditions.checkNotNull(captureWindow);
      if (grabber == null || !active) {
        log.warn("Grabber hasn't been initiated");
        return Optional.empty();
      }
      if (captureWindow.getWidth() == 0 || captureWindow.getHeight() == 0) {
        log.warn("Size can't be negative or zero");
        return Optional.empty();
      }
      try {
        Frame frame = grabber.grabImage();
        return Optional.of(resize(captureWindow, Java2DFrameUtils.toBufferedImage(frame)));
      } catch (FrameGrabber.Exception e) {
        return Optional.empty();
      }
    }
  }
}
