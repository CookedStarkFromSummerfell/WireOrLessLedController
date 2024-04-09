package io.kalishak;

public record RGBInstance(float red, float green, float blue) {
    public float[] asArray() {
        return new float[] { red(), green(), blue() };
    }
}
