package com.atm;

import javafx.animation.Interpolator;

public class SineInterpolator extends Interpolator
{
    @Override
    protected double curve(double t) {
        // Apply sine function for smooth interpolation
        return 0.5 * (1 - Math.cos(t * Math.PI));
    }
}
