// Generated by view binder compiler. Do not edit!
package com.example.tfc.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.tfc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityInfoEjerciciosBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FloatingActionButton botonRestarPeso;

  @NonNull
  public final FloatingActionButton botonRestarRepeticiones;

  @NonNull
  public final FloatingActionButton botonRestarSeries;

  @NonNull
  public final FloatingActionButton botonSumarPeso;

  @NonNull
  public final FloatingActionButton botonSumarRepeticiones;

  @NonNull
  public final FloatingActionButton botonSumarSeries;

  @NonNull
  public final AppCompatButton btnAnadir;

  @NonNull
  public final ImageView logo;

  @NonNull
  public final TextView tvEjercicio;

  @NonNull
  public final TextView tvNumPeso;

  @NonNull
  public final TextView tvNumRepeticiones;

  @NonNull
  public final TextView tvNumSeries;

  @NonNull
  public final TextView tvPeso;

  @NonNull
  public final TextView tvRepeticiones;

  @NonNull
  public final TextView tvSeries;

  @NonNull
  public final WebView vvReproductor;

  private ActivityInfoEjerciciosBinding(@NonNull ConstraintLayout rootView,
      @NonNull FloatingActionButton botonRestarPeso,
      @NonNull FloatingActionButton botonRestarRepeticiones,
      @NonNull FloatingActionButton botonRestarSeries, @NonNull FloatingActionButton botonSumarPeso,
      @NonNull FloatingActionButton botonSumarRepeticiones,
      @NonNull FloatingActionButton botonSumarSeries, @NonNull AppCompatButton btnAnadir,
      @NonNull ImageView logo, @NonNull TextView tvEjercicio, @NonNull TextView tvNumPeso,
      @NonNull TextView tvNumRepeticiones, @NonNull TextView tvNumSeries, @NonNull TextView tvPeso,
      @NonNull TextView tvRepeticiones, @NonNull TextView tvSeries,
      @NonNull WebView vvReproductor) {
    this.rootView = rootView;
    this.botonRestarPeso = botonRestarPeso;
    this.botonRestarRepeticiones = botonRestarRepeticiones;
    this.botonRestarSeries = botonRestarSeries;
    this.botonSumarPeso = botonSumarPeso;
    this.botonSumarRepeticiones = botonSumarRepeticiones;
    this.botonSumarSeries = botonSumarSeries;
    this.btnAnadir = btnAnadir;
    this.logo = logo;
    this.tvEjercicio = tvEjercicio;
    this.tvNumPeso = tvNumPeso;
    this.tvNumRepeticiones = tvNumRepeticiones;
    this.tvNumSeries = tvNumSeries;
    this.tvPeso = tvPeso;
    this.tvRepeticiones = tvRepeticiones;
    this.tvSeries = tvSeries;
    this.vvReproductor = vvReproductor;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityInfoEjerciciosBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityInfoEjerciciosBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_info_ejercicios, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityInfoEjerciciosBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.botonRestarPeso;
      FloatingActionButton botonRestarPeso = ViewBindings.findChildViewById(rootView, id);
      if (botonRestarPeso == null) {
        break missingId;
      }

      id = R.id.botonRestarRepeticiones;
      FloatingActionButton botonRestarRepeticiones = ViewBindings.findChildViewById(rootView, id);
      if (botonRestarRepeticiones == null) {
        break missingId;
      }

      id = R.id.botonRestarSeries;
      FloatingActionButton botonRestarSeries = ViewBindings.findChildViewById(rootView, id);
      if (botonRestarSeries == null) {
        break missingId;
      }

      id = R.id.botonSumarPeso;
      FloatingActionButton botonSumarPeso = ViewBindings.findChildViewById(rootView, id);
      if (botonSumarPeso == null) {
        break missingId;
      }

      id = R.id.botonSumarRepeticiones;
      FloatingActionButton botonSumarRepeticiones = ViewBindings.findChildViewById(rootView, id);
      if (botonSumarRepeticiones == null) {
        break missingId;
      }

      id = R.id.botonSumarSeries;
      FloatingActionButton botonSumarSeries = ViewBindings.findChildViewById(rootView, id);
      if (botonSumarSeries == null) {
        break missingId;
      }

      id = R.id.btnAnadir;
      AppCompatButton btnAnadir = ViewBindings.findChildViewById(rootView, id);
      if (btnAnadir == null) {
        break missingId;
      }

      id = R.id.logo;
      ImageView logo = ViewBindings.findChildViewById(rootView, id);
      if (logo == null) {
        break missingId;
      }

      id = R.id.tvEjercicio;
      TextView tvEjercicio = ViewBindings.findChildViewById(rootView, id);
      if (tvEjercicio == null) {
        break missingId;
      }

      id = R.id.tvNumPeso;
      TextView tvNumPeso = ViewBindings.findChildViewById(rootView, id);
      if (tvNumPeso == null) {
        break missingId;
      }

      id = R.id.tvNumRepeticiones;
      TextView tvNumRepeticiones = ViewBindings.findChildViewById(rootView, id);
      if (tvNumRepeticiones == null) {
        break missingId;
      }

      id = R.id.tvNumSeries;
      TextView tvNumSeries = ViewBindings.findChildViewById(rootView, id);
      if (tvNumSeries == null) {
        break missingId;
      }

      id = R.id.tvPeso;
      TextView tvPeso = ViewBindings.findChildViewById(rootView, id);
      if (tvPeso == null) {
        break missingId;
      }

      id = R.id.tvRepeticiones;
      TextView tvRepeticiones = ViewBindings.findChildViewById(rootView, id);
      if (tvRepeticiones == null) {
        break missingId;
      }

      id = R.id.tvSeries;
      TextView tvSeries = ViewBindings.findChildViewById(rootView, id);
      if (tvSeries == null) {
        break missingId;
      }

      id = R.id.vvReproductor;
      WebView vvReproductor = ViewBindings.findChildViewById(rootView, id);
      if (vvReproductor == null) {
        break missingId;
      }

      return new ActivityInfoEjerciciosBinding((ConstraintLayout) rootView, botonRestarPeso,
          botonRestarRepeticiones, botonRestarSeries, botonSumarPeso, botonSumarRepeticiones,
          botonSumarSeries, btnAnadir, logo, tvEjercicio, tvNumPeso, tvNumRepeticiones, tvNumSeries,
          tvPeso, tvRepeticiones, tvSeries, vvReproductor);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
