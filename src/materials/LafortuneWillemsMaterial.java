/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package materials;

import java.awt.Color;
import lights.Light;
import lights.Lights;
import primitives.Group;
import ray_tracer.Hit;
import ray_tracer.Ray;
import reflectances.GlossyReflectance;
import utils.Point3D;
import utils.Vector3D;

/**
 *
 * @author mikel.uriz
 */
public class LafortuneWillemsMaterial extends Material {
    
    
    public LafortuneWillemsMaterial(final Color ka, final Color ks, final Color kd, final GlossyReflectance Fs, final boolean especular) {
        
        super(ka, ks, kd, Fs, especular);

    }

    @Override
    public Color getColor(Group G, Lights lights, Point3D P, Vector3D normal, Point3D V) {
        
        Color c = this.ambientColor(lights.getAmbientalIrradiance());
        
        for(Light L : lights.getLights()) {
            c = addColors(c, this.directShader(G, P, normal, V, L));
        }
        
        if (this.isEspecular()) {
            // R = v - 2*(n dor v)*n
            // v: vector de trazado primario
            final Vector3D v = new Vector3D(V, P);
            final Vector3D R = v.substract(normal.getScaled(2*normal.dotProduct(v)));
            R.normalize();
            // El rayo parte de P con direccion R
            final Ray rmirror = new Ray(P, R);
            final Hit h = G.intersect(rmirror, 0);
            if (h.hits()) {
                final Material m = h.getMaterial();
                final Point3D Ps = h.getPoint();
                final Vector3D ns = h.getNormal();
                return addColors(c, getColor(G, lights, Ps, ns, P, 5));
            } else {

                return c;

            }
            
        }
        
        return c;
        
    }
    
    private Color getColor(Group G, Lights lights, Point3D P, Vector3D normal, Point3D V, int n) {
        //Color c = this.ambientColor(lights.getAmbientalIrradiance());
        Color c = new Color(0,0,0);
        if (n <= 0) {
            return c;
        }
        for (Light L : lights.getLights()) {
            c = addColors(c, this.directShader(G, P, normal, V, L));
        }
        
        if (this.isEspecular()) {
        
            // R = v - 2*(n dor v)*n
            // v: vector de trazado primario
            final Vector3D v = new Vector3D(V, P);
            final Vector3D R = v.substract(normal.getScaled(2*normal.dotProduct(v)));
            R.normalize();
            // El rayo parte de P con direccion R
            final Ray rmirror = new Ray(P, R);
            final Hit h = G.intersect(rmirror, 0);
            if (h.hits()) {
                final Material m = h.getMaterial();
                final Point3D Ps = h.getPoint();
                final Vector3D ns = h.getNormal();
                return addColors(c, getColor(G, lights, Ps, ns, P, n-1));
            } else {

                return c;

            }
        }
        
        return c;
        
    }
    
    private Color ambientColor(final float ambientalIrradiance) {
        
        return multColor(this.getKa(), ambientalIrradiance);
        //return new Color((int) (ka*baseColor.getRGB()*ambientalIrradiance));
        
    }
    
    private Color directShader(final Group G, final Point3D P, final Vector3D normal, final Point3D V, final Light L) {
        //(kd/PI+ks*((q+2)/(2*PI))*Fs
        final Vector3D PV = new Vector3D(P, V);
        PV.normalize();
        Color p_kd = multColor(this.getKd(), (float) (1/Math.PI));
        float mult = (float) ((this.getFs().getQ()+2)/(2*Math.PI));
        Color p_ks = multColor(this.getKs(), mult);
        Color ret_color = addColors(p_kd, p_ks);
        return multColor(ret_color, L.getIrradiance(G, P, normal));
        
        
        //return multColor(baseColor, mult);
        //return new Color((int) (baseColor.getRGB() * mult * L.getIrradiance(G, P, normal)));
        //Color retColor = addColors(kd, new Color((int) (ks.getRGB()*mult)));
        //return new Color((int) (retColor.getRGB()*L.getIrradiance()));
    }
    
}
