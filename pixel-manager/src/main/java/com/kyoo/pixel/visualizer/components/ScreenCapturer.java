package com.kyoo.pixel.visualizer.components;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
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
public final class ScreenCapturer implements Capturer {

  private Grabber grabber;
  private Rectangle rectangle;

  @Inject
  public ScreenCapturer(@Named("capturer.frame.width") int frameWidth,
      @Named("capturer.frame.height") int frameHeight) {
    rectangle = new Rectangle(0, 0, 100, 100);
    grabber = new Grabber();
    grabber.startGrabber(frameWidth, frameHeight);
  }

  @Override
  public Optional<BufferedImage> getFrame() {
    return grabber.capture(rectangle);
  }

  @Override
  public void stop() {
    grabber.stopGrabber();
  }

  public void updateRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
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

    protected Optional<BufferedImage> capture(Rectangle rectangle) {
      Preconditions.checkNotNull(rectangle);
      if (grabber == null || !active) {
        log.warn("Grabber hasn't been initiated");
        return Optional.empty();
      }
      if(rectangle.getWidth() == 0 || rectangle.getHeight()==0){
        log.warn("Rectangle can't be negative or zero width or height");
        return Optional.empty();
      }
      try {
        Frame frame = grabber.grabImage();
        return Optional.of(resize(rectangle, Java2DFrameUtils.toBufferedImage(frame)));
      } catch (FrameGrabber.Exception e) {
        return Optional.empty();
      }
    }

    private BufferedImage resize(Rectangle rectangle, BufferedImage image) {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      double widthRatio = (double) image.getWidth() / screenSize.width;
      double heightRatio = (double) image.getHeight() / screenSize.height;
      int x = (int) (
          Math.min(Math.max(rectangle.getX(), 0), screenSize.width - (int) rectangle.getWidth())
              * widthRatio);
      int y = (int) (
          Math.min(Math.max(rectangle.getY(), 0), screenSize.height - (int) rectangle.getHeight())
              * heightRatio);
      int width = (int) (rectangle.getWidth() * widthRatio);
      int height = (int) (rectangle.getHeight() * heightRatio);
      return image.getSubimage(x, y, width, height);
    }
  }
}
