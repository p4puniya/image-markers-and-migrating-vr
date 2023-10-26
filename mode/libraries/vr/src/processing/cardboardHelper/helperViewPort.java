package processing.cardboardHelper;

import android.opengl.GLES20;
public class helperViewPort {

    public int x;
    public int y;
    public int width;
    public int height;

    public helperViewPort() {
    }

    public void setViewport(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setGLViewport() {
        GLES20.glViewport(this.x, this.y, this.width, this.height);
    }

    public void setGLScissor() {
        GLES20.glScissor(this.x, this.y, this.width, this.height);
    }

    public void getAsArray(int[] array, int offset) {
        if (offset + 4 > array.length) {
            throw new IllegalArgumentException("Not enough space to write the result");
        } else {
            array[offset] = this.x;
            array[offset + 1] = this.y;
            array[offset + 2] = this.width;
            array[offset + 3] = this.height;
        }
    }

    public String toString() {
        StringBuilder var10000 = (new StringBuilder()).append("{\n");
        int var1 = this.x;
        var10000 = var10000.append((new StringBuilder(18)).append("  x: ").append(var1).append(",\n").toString());
        var1 = this.y;
        var10000 = var10000.append((new StringBuilder(18)).append("  y: ").append(var1).append(",\n").toString());
        var1 = this.width;
        var10000 = var10000.append((new StringBuilder(22)).append("  width: ").append(var1).append(",\n").toString());
        var1 = this.height;
        return var10000.append((new StringBuilder(23)).append("  height: ").append(var1).append(",\n").toString()).append("}").toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof helperViewPort)) {
            return false;
        } else {
            helperViewPort other = (helperViewPort) obj;
            return this.x == other.x && this.y == other.y && this.width == other.width && this.height == other.height;
        }
    }

    public int hashCode() {
        return Integer.valueOf(this.x).hashCode() ^ Integer.valueOf(this.y).hashCode() ^ Integer.valueOf(this.width).hashCode() ^ Integer.valueOf(this.height).hashCode();
    }
}
