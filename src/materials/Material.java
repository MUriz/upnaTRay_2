package materials;


import java.awt.Color;
import lights.Lights;
import primitives.Group;
import reflectances.GlossyReflectance;
import utils.Point3D;
import utils.Vector3D;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author txumauriz
 */
public abstract class Material {
    
    private final Color ka;
    private final Color ks;
    private final Color kd;
    private final GlossyReflectance Fs;
    private final boolean especular;
    
    public Material (final Color ka, final Color ks, final Color kd, final GlossyReflectance Fs, final boolean especular) {
        this.ka = ka;
        this.ks = ks;
        this.kd = kd;
        this.Fs = Fs;
        this.especular = especular;
    }
    
    protected Color addColors(final Color c1, final Color c2) {
        final int red = Math.min(255, c1.getRed() + c2.getRed());
        final int green = Math.min(255, c1.getGreen() + c2.getGreen());
        final int blue = Math.min(255, c1.getBlue() + c2.getBlue());
        return new Color(red, green, blue);
        //return new Color(c1.getRGB() + c2.getRGB());
    }
    
    protected Color multColor(final Color c, final float f) {
        int red = Math.min(255, (int)(c.getRed()*f));
        int green = Math.min(255, (int)(c.getGreen()*f));
        int blue = Math.min(255, (int)(c.getBlue()*f));
        return new Color(red, green, blue);
    }

    public Color getKa() {
        return ka;
    }

    public Color getKs() {
        return ks;
    }

    public Color getKd() {
        return kd;
    }

    public GlossyReflectance getFs() {
        return Fs;
    }
    
    public boolean isEspecular() {
        return especular;
    }
    
    public abstract Color getColor(final Group G, final Lights L, final Point3D P,
                                    final Vector3D normal, final Point3D V);
    
}