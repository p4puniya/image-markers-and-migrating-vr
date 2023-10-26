package processing.cardboardHelper;

import processing.cardboardHelper.helperEye;
import processing.cardboardHelper.helperFieldOfView;
public class helperEye {

    private final int type;
    private final float[] eyeView;
    private final helperViewPort viewport;
    private final helperFieldOfView fov;
    private volatile boolean projectionChanged;
    private float[] perspective;
    private float lastZNear;
    private float lastZFar;

    public helperEye(int type) {
        this.type = type;
        this.eyeView = new float[16];
        this.viewport = new helperViewPort();
        this.fov = new helperFieldOfView();
        this.projectionChanged = true;
    }

    public int getType() {
        return this.type;
    }


    public float[] getEyeView() {
        return this.eyeView;
    }

    public float[] getPerspective(float zNear, float zFar) {
        if (!this.projectionChanged && this.lastZNear == zNear && this.lastZFar == zFar) {
            return this.perspective;
        } else {
            if (this.perspective == null) {
                this.perspective = new float[16];
            }

            this.getFov().toPerspectiveMatrix(zNear, zFar, this.perspective, 0);
            this.lastZNear = zNear;
            this.lastZFar = zFar;
            this.projectionChanged = false;
            return this.perspective;
        }
    }

    public helperViewPort getViewport() {
        return this.viewport;
    }

    public helperFieldOfView getFov() {
        return this.fov;
    }

    public void setProjectionChanged() {
        this.projectionChanged = true;
    }

    public boolean getProjectionChanged() {
        return this.projectionChanged;
    }


    private void setValues(int viewportX, int viewportY, int viewportWidth, int viewportHeight, float fovLeft, float fovRight, float fovBottom, float fovTop) {
        this.viewport.setViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        this.fov.setAngles(fovLeft, fovRight, fovBottom, fovTop);
        this.projectionChanged = true;
    }

    public abstract static class Type {
        public static final int MONOCULAR = 0;
        public static final int LEFT = 1;
        public static final int RIGHT = 2;

        public Type() {
        }
    }

}
