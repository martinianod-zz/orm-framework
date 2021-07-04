package utilidades;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class UBean {

	public static ArrayList<Field> obtenerAtributos(Object o) {

		ArrayList<Field> arrayField = new ArrayList<Field>();

		Class<? extends Object> c = o.getClass();

		return new ArrayList<Field>(Arrays.asList(c.getDeclaredFields()));

	}

	public static void ejecutarSet(Object o, String att, Object valor) {

		Class<? extends Object> c = o.getClass();

		for (Field f : c.getDeclaredFields()) {

			String setter = "set".concat(att.substring(0, 1).toUpperCase()).concat(att.substring(1).toLowerCase());

			System.out.println(setter);

			for (Method m : c.getDeclaredMethods()) {

				if (m.getName().equals(setter)) {

					Object[] parametros = new Object[1];

					try {
						parametros[0] = valor;

						m.invoke(o, parametros);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

						e.printStackTrace();
					}

					break;
				}

			}

		}

	}

	public static Object ejecutarGet(Object o, String att) {

		Object getResult = null;

		Class<? extends Object> c = o.getClass();

		for (Field f : c.getDeclaredFields()) {

			String getter = "get".concat(att.substring(0, 1).toUpperCase()).concat(att.substring(1).toLowerCase());

			System.out.println(getter);

			for (Method m : c.getDeclaredMethods()) {

				if (m.getName().equals(getter)) {

					try {

						getResult = m.invoke(o, null);

					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

						e.printStackTrace();
					}

					break;
				}

			}

		}

		return getResult;

	}

}
