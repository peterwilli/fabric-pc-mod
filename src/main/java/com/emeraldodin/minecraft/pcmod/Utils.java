package com.emeraldodin.minecraft.pcmod;

import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class Utils {
    public static Quaternion lookAt(Vec3d src, Vec3d dest) {
        Vec3d forward = new Vec3d(0,0,1);
        Vec3d fwd = src.subtract(dest).normalize();
        double dot = forward.dotProduct(fwd);
        Vec3d up = new Vec3d(0,1,0);
        if(Math.abs(dot - (-1.0f)) < 0.000001f) {
            return new Quaternion((float)up.x, (float)up.y, (float)up.z, 3.1415926535897932f);
        }
        if(Math.abs(dot - (1.0f)) < 0.000001f) {
            return Quaternion.IDENTITY;
        }

        double rotAngle = Math.acos(dot);
        Vec3d rotAxis = forward.crossProduct(fwd);
        rotAxis = rotAxis.normalize();
        return createFromAxisAngle(rotAxis, rotAngle);
    }

    public static Quaternion createFromAxisAngle(Vec3d axis, double angle) {
        double halfAngle = angle * .5;
        float s = (float)Math.sin(halfAngle);
        Quaternion q = new Quaternion((float) axis.x * s, (float) axis.y * s, (float) axis.z * s, (float)Math.cos(halfAngle));
        return q;
    }
}
