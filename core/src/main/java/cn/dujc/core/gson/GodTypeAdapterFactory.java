package cn.dujc.core.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * https://github.com/dampcake/gson-immutable/blob/master/src/main/java/com/dampcake/gson/immutable/ImmutableAdapterFactory.java
 */
public class GodTypeAdapterFactory {

    public static GsonBuilder createBuilder() {
        return GodTypeAdapterFactory.createBuilder(false);
    }

    public static GsonBuilder createBuilder(boolean useMinPrimitive) {
        return GodTypeAdapterFactory.assess(useMinPrimitive, new GsonBuilder());
    }

    public static GsonBuilder assess(boolean useMinPrimitive, GsonBuilder builder) {
        builder.registerTypeAdapterFactory(TypeAdapters.JSON_ELEMENT_FACTORY);
        builder.registerTypeAdapterFactory(ObjectTypeAdapter.FACTORY);

        // type adapters for basic platform types
        builder.registerTypeAdapterFactory(TypeAdapters.STRING_FACTORY);
        builder.registerTypeAdapterFactory(useMinPrimitive ? TypeAdapters.INTEGER_FACTORY : TypeAdapters.INTEGER_FACTORY0);
        builder.registerTypeAdapterFactory(TypeAdapters.BOOLEAN_FACTORY);
        builder.registerTypeAdapterFactory(useMinPrimitive ? TypeAdapters.BYTE_FACTORY : TypeAdapters.BYTE_FACTORY0);
        builder.registerTypeAdapterFactory(useMinPrimitive ? TypeAdapters.SHORT_FACTORY : TypeAdapters.SHORT_FACTORY0);
        TypeAdapter<Number> longAdapter = useMinPrimitive ? longAdapter(LongSerializationPolicy.DEFAULT) : longAdapter0(LongSerializationPolicy.DEFAULT);
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, longAdapter));
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(double.class, Double.class,
                useMinPrimitive ? doubleAdapter(false) : doubleAdapter0(false)));
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(float.class, Float.class,
                useMinPrimitive ? floatAdapter(false) : floatAdapter0(false)));
        builder.registerTypeAdapterFactory(TypeAdapters.NUMBER_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
        builder.registerTypeAdapterFactory(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.CHARACTER_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.STRING_BUILDER_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.STRING_BUFFER_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        builder.registerTypeAdapterFactory(TypeAdapters.URL_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.URI_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.UUID_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.CURRENCY_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.LOCALE_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.INET_ADDRESS_FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.BIT_SET_FACTORY);
        builder.registerTypeAdapterFactory(DateTypeAdapter.FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.CALENDAR_FACTORY);
        builder.registerTypeAdapterFactory(TimeTypeAdapter.FACTORY);
        builder.registerTypeAdapterFactory(SqlDateTypeAdapter.FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.TIMESTAMP_FACTORY);
        builder.registerTypeAdapterFactory(ArrayTypeAdapter.FACTORY);
        builder.registerTypeAdapterFactory(TypeAdapters.CLASS_FACTORY);

        // type adapters for composite and user-defined types
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
        builder.registerTypeAdapterFactory(new CollectionTypeAdapterFactory(
                constructorConstructor));
        builder.registerTypeAdapterFactory(new MapTypeAdapterFactory(constructorConstructor, false));
        JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        builder.registerTypeAdapterFactory(jsonAdapterFactory);
        builder.registerTypeAdapterFactory(TypeAdapters.ENUM_FACTORY);
//        builder.registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
//                constructorConstructor, FieldNamingPolicy.IDENTITY, Excluder.DEFAULT, jsonAdapterFactory));
        return builder;
    }

    private static void checkValidFloatingPoint(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(value
                    + " is not a valid double value as per JSON specification. To override this"
                    + " behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }

    private static TypeAdapter<Number> doubleAdapter0(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Double read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                try {
                    return in.nextDouble();
                } catch (Exception e) {
                    in.skipValue();
                    return null;
                }
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                double doubleValue = value.doubleValue();
                checkValidFloatingPoint(doubleValue);
                out.value(value);
            }
        };
    }

    private static TypeAdapter<Number> doubleAdapter(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Double read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                try {
                    return in.nextDouble();
                } catch (Exception e) {
                    in.skipValue();
                    return Double.MIN_VALUE;
                }
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                double doubleValue = value.doubleValue();
                checkValidFloatingPoint(doubleValue);
                out.value(value);
            }
        };
    }

    private static TypeAdapter<Number> floatAdapter0(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Float read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                try {
                    return (float) in.nextDouble();
                } catch (Exception e) {
                    in.skipValue();
                    return null;
                }
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                float floatValue = value.floatValue();
                checkValidFloatingPoint(floatValue);
                out.value(value);
            }
        };
    }

    private static TypeAdapter<Number> floatAdapter(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Float read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                try {
                    return (float) in.nextDouble();
                } catch (Exception e) {
                    in.skipValue();
                    return Float.MIN_VALUE;
                }
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                float floatValue = value.floatValue();
                checkValidFloatingPoint(floatValue);
                out.value(value);
            }
        };
    }

    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> longAdapter) {
        return new TypeAdapter<AtomicLong>() {
            @Override
            public void write(JsonWriter out, AtomicLong value) throws IOException {
                longAdapter.write(out, value.get());
            }

            @Override
            public AtomicLong read(JsonReader in) throws IOException {
                Number value = longAdapter.read(in);
                return new AtomicLong(value.longValue());
            }
        }.nullSafe();
    }

    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> longAdapter) {
        return new TypeAdapter<AtomicLongArray>() {
            @Override
            public void write(JsonWriter out, AtomicLongArray value) throws IOException {
                out.beginArray();
                for (int i = 0, length = value.length(); i < length; i++) {
                    longAdapter.write(out, value.get(i));
                }
                out.endArray();
            }

            @Override
            public AtomicLongArray read(JsonReader in) throws IOException {
                List<Long> list = new ArrayList<Long>();
                in.beginArray();
                while (in.hasNext()) {
                    long value = longAdapter.read(in).longValue();
                    list.add(value);
                }
                in.endArray();
                int length = list.size();
                AtomicLongArray array = new AtomicLongArray(length);
                for (int i = 0; i < length; ++i) {
                    array.set(i, list.get(i));
                }
                return array;
            }
        }.nullSafe();
    }

    private static TypeAdapter<Number> longAdapter0(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Number read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                try {
                    return in.nextLong();
                } catch (Exception e) {
                    in.skipValue();
                    return null;
                }
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(value.toString());
            }
        };
    }

    private static TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Number read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                try {
                    return in.nextLong();
                } catch (Exception e) {
                    in.skipValue();
                    return Long.MIN_VALUE;
                }
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(value.toString());
            }
        };
    }

}
