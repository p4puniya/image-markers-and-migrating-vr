/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  Part of the Processing project - http://processing.org

  Copyright (c) 2019 The Processing Foundation

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation, version 2.1.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
*/

package processing.ar;

import android.graphics.Bitmap;

import com.google.ar.core.HitResult;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.io.*;

import processing.core.PApplet;
import processing.core.PImage;

public class ARTracker {
  private static final String TAG = ARTracker.class.getSimpleName();
  protected PApplet p;
  protected ARGraphics g,arg;
  protected AugmentedImageDatabase imgDB;

  private HashMap<String, ARTrackable> trackables = new HashMap<String, ARTrackable>();
  private ArrayList<ARAnchor> toRemove = new ArrayList<ARAnchor>();
  private Method trackableEventMethod;
  private Session session;

  public ARTracker(PApplet parent) {
    this.p = parent;
    this.g = (ARGraphics) p.g;
    setEventHandler();
    this.arg = (ARGraphics)parent.g;//A new Database has been created.

    session = arg.surfar.session;

    this.imgDB = new AugmentedImageDatabase(session);
  }

  public void addImage(String name, PImage img) {
    System.out.println("code started");
    Log.d(TAG, "Function Started.");
          // Check if PImage is null
    if (img == null) {
      System.err.println("PImage is null. Failed to load the image.");
      return;
    }
          // Convert PImage to Bitmap
    Bitmap bitmap = (Bitmap) img.getNative();
          // Check if Bitmap is null
    if (bitmap == null) {
      System.err.println("Bitmap is null. Failed to load the image.");
      return;
    }
          // Check if Bitmap dimensions are within ARCore's requirements
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();
          // Set a safe maximum image resolution for augmented image tracking
    int maxImageDimension = 1024;

    if (width > maxImageDimension || height > maxImageDimension) {
      System.err.println("Image dimensions exceed the maximum supported resolution for ARCore.");
      return;
    }
          // Check if Bitmap format is ARGB_8888
    if (bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
      System.err.println("Bitmap format is not ARGB_8888. Required format for ARCore image tracking.");
      return;
    }

    int imgIndex = imgDB.addImage(name, bitmap);
    System.out.println("imgIndex has been serialized");
    System.out.println("ChatGPT-1 code");

    Config config= new Config(session);
        // Re-set the session config with the updated image database
     System.out.println("session reset and configured");
    config.setAugmentedImageDatabase(imgDB);
    System.out.println("augmentedImageDatabase set");
    session.configure(config);
        // System.out.println("code complete");
  }

  public void start() {
    cleanup();
    g.addTracker(this);
  }

  public void stop() {
    g.removeTracker(this);
  }

  public int count() {
    return g.trackableCount();
  }

  public ARTrackable get(int idx) {
    int id = g.trackableId(idx);
    String sid = String.valueOf(id);
    if (!trackables.containsKey(sid)) {
      ARTrackable t = new ARTrackable(g, id);
      trackables.put(sid, t);
    }
    return get(sid);
  }

  public ARTrackable get(String id) {
    return trackables.get(id);
  }

  public ARTrackable get(int mx, int my) {
    HitResult hit = g.getHitResult(mx, my);
    if (hit != null) {
      int idx = g.getTrackable(hit);
      ARTrackable t = get(idx);
      t.hit = hit;
      return t;
    } else {
      return null;
    }
  }

  protected void create(int idx) {
    if (trackableEventMethod != null) {
      try {
        ARTrackable t = get(idx);
        trackableEventMethod.invoke(p, t);
      } catch (Exception e) {
        System.err.println("error, disabling trackableEventMethod() for AR tracker");
        e.printStackTrace();
        trackableEventMethod = null;
      }
    }
  }

  public void clearAnchors(Collection<ARAnchor> anchors) {
    for (ARAnchor anchor : anchors) {
      if (anchor.isStopped() || anchor.isDisposed()) {
        anchor.dispose();
        toRemove.add(anchor);
      }
    }
    anchors.removeAll(toRemove);
    toRemove.clear();
  }

  protected void cleanup() {
    // Remove any inactive trackables left over in the tracker.
    Set<String> ids = trackables.keySet();
    for (String id: ids) {
      ARTrackable t = trackables.get(id);
      if (t.isStopped()) trackables.remove(id);
    }
  }

  protected void remove(int idx) {
    int id = g.trackableId(idx);
    String sid = String.valueOf(id);
    remove(sid);
  }

  protected void remove(String id) {
    trackables.remove(id);
  }


  protected void setEventHandler() {
    try {
      trackableEventMethod = p.getClass().getMethod("trackableEvent", ARTrackable.class);
      return;
    } catch (Exception e) {
      // no such method, or an error... which is fine, just ignore
    }

    // trackableEvent can alternatively be defined as receiving an Object, to allow
    // Processing mode implementors to support the video library without linking
    // to it at build-time.
    try {
      trackableEventMethod = p.getClass().getMethod("trackableEvent", Object.class);
    } catch (Exception e) {
      // no such method, or an error... which is fine, just ignore
    }
  }
}