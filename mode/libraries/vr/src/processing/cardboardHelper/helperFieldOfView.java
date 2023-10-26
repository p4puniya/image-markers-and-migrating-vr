package processing.cardboardHelper;

import android.opengl.Matrix;
public class helperFieldOfView {

    private static final float CARDBOARD_V2_2_MAX_FOV_LEFT_RIGHT = 60.0F;
    private static final float CARDBOARD_V2_2_MAX_FOV_BOTTOM = 60.0F;
    private static final float CARDBOARD_V2_2_MAX_FOV_TOP = 60.0F;
    private static final float CARDBOARD_V1_MAX_FOV_LEFT_RIGHT = 40.0F;
    private static final float CARDBOARD_V1_MAX_FOV_BOTTOM = 40.0F;
    private static final float CARDBOARD_V1_MAX_FOV_TOP = 40.0F;
    private float left;
    private float right;
    private float bottom;
    private float top;

    public helperFieldOfView() {
        this.left = 60.0F;
        this.right = 60.0F;
        this.bottom = 60.0F;
        this.top = 60.0F;
    }

    public static helperFieldOfView cardboardV1FieldOfView() {
        helperFieldOfView params = new helperFieldOfView();
        params.setAngles(40.0F, 40.0F, 40.0F, 40.0F);
        return params;
    }

    public helperFieldOfView(float left, float right, float bottom, float top) {
        this.setAngles(left, right, bottom, top);
    }

    public helperFieldOfView(helperFieldOfView other) {
        this.copy(other);
    }

    public static helperFieldOfView parseFromProtobuf(float[] angles) {
        return angles.length != 4 ? null : new helperFieldOfView(angles[0], angles[1], angles[2], angles[3]);
    }

    public float[] toProtobuf() {
        return new float[]{this.left, this.right, this.bottom, this.top};
    }

    public void copy(helperFieldOfView other) {
        this.left = other.left;
        this.right = other.right;
        this.bottom = other.bottom;
        this.top = other.top;
    }

    public void setAngles(float left, float right, float bottom, float top) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getLeft() {
        return this.left;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getRight() {
        return this.right;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getBottom() {
        return this.bottom;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getTop() {
        return this.top;
    }

    public void toPerspectiveMatrix(float near, float far, float[] perspective, int offset) {
        if (offset + 16 > perspective.length) {
            throw new IllegalArgumentException("Not enough space to write the result");
        } else {
            float l = (float)(-Math.tan(Math.toRadians((double)this.left))) * near;
            float r = (float)Math.tan(Math.toRadians((double)this.right)) * near;
            float b = (float)(-Math.tan(Math.toRadians((double)this.bottom))) * near;
            float t = (float)Math.tan(Math.toRadians((double)this.top)) * near;
            Matrix.frustumM(perspective, offset, l, r, b, t, near, far);
        }
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else if (!(other instanceof helperFieldOfView)) {
            return false;
        } else {
            helperFieldOfView o = (helperFieldOfView)other;
            return this.left == o.left && this.right == o.right && this.bottom == o.bottom && this.top == o.top;
        }
    }

    public String toString() {
        StringBuilder var10000 = (new StringBuilder()).append("{\n");
        float var1 = this.left;
        var10000 = var10000.append((new StringBuilder(25)).append("  left: ").append(var1).append(",\n").toString());
        var1 = this.right;
        var10000 = var10000.append((new StringBuilder(26)).append("  right: ").append(var1).append(",\n").toString());
        var1 = this.bottom;
        var10000 = var10000.append((new StringBuilder(27)).append("  bottom: ").append(var1).append(",\n").toString());
        var1 = this.top;
        return var10000.append((new StringBuilder(24)).append("  top: ").append(var1).append(",\n").toString()).append("}").toString();
    }
}
