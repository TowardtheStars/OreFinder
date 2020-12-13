package towardsthestars.orefinder.util;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum Plain8Direction implements IPlainDirection
{
    NORTHWEST("northwest", 7),
    NORTH("north", 6),
    NORTHEAST("northeast", 5),
    EAST("east", 4),
    SOUTHEAST("southeast", 3),
    SOUTH("south", 2),
    SOUTHWEST("southwest", 1),
    WEST("west", 0)
    ;

    public static final Plain8Direction[] DIRECTIONS8 =
            {
                    WEST, SOUTHWEST, SOUTH, SOUTHEAST, EAST, NORTHEAST, NORTH, NORTHWEST
            };

    public static final Plain8Direction[] DIRECTIONS4 = {WEST, SOUTH, EAST, NORTH};

    String translationKey;
    byte id;

    Plain8Direction(String key, int id)
    {
        this.translationKey = "orefinder.direction." + key;
        this.id = (byte) id;
    }



    public ITextComponent getTranslationTextComponent()
    {
        return new TranslationTextComponent(translationKey);
    }


    public ByteNBT getNBT()
    {
        return ByteNBT.valueOf(this.id);
    }



    public static Plain8Direction fromNBT(ByteNBT nbt)
    {
        return DIRECTIONS8[nbt.getInt() % 8];
    }
    /**
     * Pick direction, Orient is oriental
     *
     * @param angle angle in degrees
     * @return Direction
     */
    public static Plain8Direction fromDegree8(double angle)
    {
        return DIRECTIONS8[((int)(Math.floor((MathHelper.wrapDegrees(angle) + 202.5D) / 45.0D))) % 8];
    }

    public static Plain8Direction fromRad8(double angle)
    {
        return fromDegree8(Math.toDegrees(angle));
    }

    public static Plain8Direction fromVec2f8(Vec2f vec)
    {
        double rad = Math.acos(vec.x / (Math.pow(vec.x, 2) + Math.pow(vec.y, 2)));
        if (vec.y < 0)
        {
            rad = -rad;
        }
        return fromRad8(rad);
    }

    public static Plain8Direction fromDegree4(double angle)
    {
        return DIRECTIONS4[
                (
                        ((int)
                                (Math.floor(
                                        (MathHelper.wrapDegrees(angle) + 225.0D) / 90.0D
                                ))
                        ) % 4
                )
                ];
    }

    public static Plain8Direction fromRad4(double angle)
    {
        return fromDegree4(Math.toDegrees(angle));
    }

    public static Plain8Direction fromVec2f4(Vec2f vec)
    {
        double rad = Math.acos(vec.x / (Math.pow(vec.x, 2) + Math.pow(vec.y, 2)));
        if (vec.y < 0)
        {
            rad = -rad;
        }
        return fromRad4(rad);
    }
}
