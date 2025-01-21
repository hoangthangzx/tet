package com.metalsensor.gold.detector.finder;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.metalsensor.gold.detector.finder.databinding.ActivityAboutBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityCoinrecognitionBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityGoldDetecterBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityHomeBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityInteractBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityIntroBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityLanguageBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityMetalDetectorBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityMoreBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivityPermissionBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivitySelectThemeBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivitySettingBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.ActivitySplashBindingImpl;
import com.metalsensor.gold.detector.finder.databinding.DialogRateBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYABOUT = 1;

  private static final int LAYOUT_ACTIVITYCOINRECOGNITION = 2;

  private static final int LAYOUT_ACTIVITYGOLDDETECTER = 3;

  private static final int LAYOUT_ACTIVITYHOME = 4;

  private static final int LAYOUT_ACTIVITYINTERACT = 5;

  private static final int LAYOUT_ACTIVITYINTRO = 6;

  private static final int LAYOUT_ACTIVITYLANGUAGE = 7;

  private static final int LAYOUT_ACTIVITYMETALDETECTOR = 8;

  private static final int LAYOUT_ACTIVITYMORE = 9;

  private static final int LAYOUT_ACTIVITYPERMISSION = 10;

  private static final int LAYOUT_ACTIVITYSELECTTHEME = 11;

  private static final int LAYOUT_ACTIVITYSETTING = 12;

  private static final int LAYOUT_ACTIVITYSPLASH = 13;

  private static final int LAYOUT_DIALOGRATE = 14;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(14);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_about, LAYOUT_ACTIVITYABOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_coinrecognition, LAYOUT_ACTIVITYCOINRECOGNITION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_gold_detecter, LAYOUT_ACTIVITYGOLDDETECTER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_home, LAYOUT_ACTIVITYHOME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_interact, LAYOUT_ACTIVITYINTERACT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_intro, LAYOUT_ACTIVITYINTRO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_language, LAYOUT_ACTIVITYLANGUAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_metal_detector, LAYOUT_ACTIVITYMETALDETECTOR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_more, LAYOUT_ACTIVITYMORE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_permission, LAYOUT_ACTIVITYPERMISSION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_select_theme, LAYOUT_ACTIVITYSELECTTHEME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_setting, LAYOUT_ACTIVITYSETTING);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.activity_splash, LAYOUT_ACTIVITYSPLASH);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.metalsensor.gold.detector.finder.R.layout.dialog_rate, LAYOUT_DIALOGRATE);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYABOUT: {
          if ("layout/activity_about_0".equals(tag)) {
            return new ActivityAboutBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_about is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYCOINRECOGNITION: {
          if ("layout/activity_coinrecognition_0".equals(tag)) {
            return new ActivityCoinrecognitionBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_coinrecognition is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYGOLDDETECTER: {
          if ("layout/activity_gold_detecter_0".equals(tag)) {
            return new ActivityGoldDetecterBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_gold_detecter is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYHOME: {
          if ("layout/activity_home_0".equals(tag)) {
            return new ActivityHomeBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_home is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYINTERACT: {
          if ("layout/activity_interact_0".equals(tag)) {
            return new ActivityInteractBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_interact is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYINTRO: {
          if ("layout/activity_intro_0".equals(tag)) {
            return new ActivityIntroBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_intro is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYLANGUAGE: {
          if ("layout/activity_language_0".equals(tag)) {
            return new ActivityLanguageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_language is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMETALDETECTOR: {
          if ("layout/activity_metal_detector_0".equals(tag)) {
            return new ActivityMetalDetectorBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_metal_detector is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMORE: {
          if ("layout/activity_more_0".equals(tag)) {
            return new ActivityMoreBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_more is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYPERMISSION: {
          if ("layout/activity_permission_0".equals(tag)) {
            return new ActivityPermissionBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_permission is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSELECTTHEME: {
          if ("layout/activity_select_theme_0".equals(tag)) {
            return new ActivitySelectThemeBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_select_theme is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSETTING: {
          if ("layout/activity_setting_0".equals(tag)) {
            return new ActivitySettingBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_setting is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSPLASH: {
          if ("layout/activity_splash_0".equals(tag)) {
            return new ActivitySplashBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_splash is invalid. Received: " + tag);
        }
        case  LAYOUT_DIALOGRATE: {
          if ("layout/dialog_rate_0".equals(tag)) {
            return new DialogRateBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for dialog_rate is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(14);

    static {
      sKeys.put("layout/activity_about_0", com.metalsensor.gold.detector.finder.R.layout.activity_about);
      sKeys.put("layout/activity_coinrecognition_0", com.metalsensor.gold.detector.finder.R.layout.activity_coinrecognition);
      sKeys.put("layout/activity_gold_detecter_0", com.metalsensor.gold.detector.finder.R.layout.activity_gold_detecter);
      sKeys.put("layout/activity_home_0", com.metalsensor.gold.detector.finder.R.layout.activity_home);
      sKeys.put("layout/activity_interact_0", com.metalsensor.gold.detector.finder.R.layout.activity_interact);
      sKeys.put("layout/activity_intro_0", com.metalsensor.gold.detector.finder.R.layout.activity_intro);
      sKeys.put("layout/activity_language_0", com.metalsensor.gold.detector.finder.R.layout.activity_language);
      sKeys.put("layout/activity_metal_detector_0", com.metalsensor.gold.detector.finder.R.layout.activity_metal_detector);
      sKeys.put("layout/activity_more_0", com.metalsensor.gold.detector.finder.R.layout.activity_more);
      sKeys.put("layout/activity_permission_0", com.metalsensor.gold.detector.finder.R.layout.activity_permission);
      sKeys.put("layout/activity_select_theme_0", com.metalsensor.gold.detector.finder.R.layout.activity_select_theme);
      sKeys.put("layout/activity_setting_0", com.metalsensor.gold.detector.finder.R.layout.activity_setting);
      sKeys.put("layout/activity_splash_0", com.metalsensor.gold.detector.finder.R.layout.activity_splash);
      sKeys.put("layout/dialog_rate_0", com.metalsensor.gold.detector.finder.R.layout.dialog_rate);
    }
  }
}
