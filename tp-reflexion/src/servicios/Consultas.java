package servicios;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import anotaciones.Columna;
import anotaciones.Id;
import anotaciones.Tabla;
import utilidades.UBean;
import utilidades.UConexion;

public class Consultas {

	Connection conexion;

	public static void guardar(Object o) throws SQLException {

		Connection c = UConexion.getInstanciaConexion();

		String tableName;

		Tabla table = o.getClass().getAnnotation(Tabla.class);

		if (table != null) {
			if (!table.nombre().replaceAll(" ", "").isEmpty()) {
				tableName = table.nombre().toLowerCase();
			} else {
				tableName = o.getClass().getSimpleName().toLowerCase();
			}
		} else {

			tableName = o.getClass().getSimpleName().toLowerCase();
		}

		String statement = "insert into " + tableName + " ( ";

		ArrayList<Field> atts = UBean.obtenerAtributos(o);

		List<Object> values = new ArrayList<>();
		String valuesIncognitas = "";

		int cantidadFields = atts.size();

		for (int i = 0; i < cantidadFields; i++) {

			Field att = atts.get(i);

			if (att.getAnnotation(Id.class) == null) {

				Columna column = att.getAnnotation(Columna.class);

				if (column != null) {
					String columnName;

					if (!column.nombre().replaceAll(" ", "").isEmpty()) {
						columnName = column.nombre();
					} else {
						columnName = att.getName();
					}

					if (cantidadFields == i + 1) {
						statement = statement.concat(columnName + " ) values ( " + valuesIncognitas + " ? );");
					} else {
						statement = statement.concat(columnName + " , ");
						valuesIncognitas = valuesIncognitas.concat("? , ");
					}

					Object value = UBean.ejecutarGet(o, att.getName());
					values.add(value);

				} else {

					// Si no tiene la anotacion columna devuelvo el nombre del campo

					if (cantidadFields == i + 1) {

						statement = statement.concat(att.getName() + " ) values ( " + valuesIncognitas + " ? );");
					} else {
						statement = statement.concat(att.getName() + " , ");
						valuesIncognitas = valuesIncognitas.concat("? , ");
					}

					Object value = UBean.ejecutarGet(o, att.getName());

					values.add(value);
				}

			}

		}

		PreparedStatement s = c.prepareStatement(statement);

		System.out.println(statement);

		for (int i = 0; i < values.size(); i++) {

			System.out.println(values.get(i));

			s.setObject(i + 1, values.get(i));

		}

		System.out.println(statement);

		s.execute();

		c.close();

	}

	public static void modificar(Object o) throws SQLException {

		Connection c = UConexion.getInstanciaConexion();

		String tableName;

		Tabla table = o.getClass().getAnnotation(Tabla.class);

		if (table != null) {
			if (!table.nombre().replaceAll(" ", "").isEmpty()) {
				tableName = table.nombre().toLowerCase();
			} else {
				tableName = o.getClass().getSimpleName().toLowerCase();
			}
		} else {

			tableName = o.getClass().getSimpleName().toLowerCase();
		}

		String statement = "update " + tableName + " set ";

		ArrayList<Field> atts = UBean.obtenerAtributos(o);

		List<Object> values = new ArrayList<>();

		int cantidadFields = atts.size();

		String idColumnName = "";
		Object idValue = null;

		for (int i = 0; i < cantidadFields; i++) {

			Field att = atts.get(i);

			if (att.getAnnotation(Id.class) == null) {

				Columna column = att.getAnnotation(Columna.class);

				if (column != null) {
					String columnName;

					if (!column.nombre().replaceAll(" ", "").isEmpty()) {
						columnName = column.nombre();
					} else {
						columnName = att.getName();
					}

					if (cantidadFields == i + 1) {
						statement = statement.concat(columnName + " =  ? ");
					} else {
						statement = statement.concat(columnName + " =  ? , ");
					}

					Object value = UBean.ejecutarGet(o, att.getName());
					values.add(value);

				} else {

					// Si no tiene la anotacion columna devuelvo el nombre del campo

					if (cantidadFields == i + 1) {

						statement = statement.concat(att.getName() + " = ? ");
					} else {
						statement = statement.concat(att.getName() + " = ? , ");
					}

					Object value = UBean.ejecutarGet(o, att.getName());

					values.add(value);
				}

			} else {
				Columna column = att.getAnnotation(Columna.class);

				if (column != null) {

					if (!column.nombre().replaceAll(" ", "").isEmpty()) {
						idColumnName = column.nombre();
					} else {
						idColumnName = att.getName();
					}

					idValue = UBean.ejecutarGet(o, att.getName());

				} else {

					idColumnName = att.getName();
					idValue = UBean.ejecutarGet(o, att.getName());

				}
			}

		}

		statement = statement.concat(" where " + idColumnName + " = " + idValue);

		PreparedStatement s = c.prepareStatement(statement);

		System.out.println(statement);

		for (int i = 0; i < values.size(); i++) {

			System.out.println(values.get(i));

			s.setObject(i + 1, values.get(i));

		}

		System.out.println(statement);

		s.execute();

		c.close();
	}

	public static void eliminar(Object o) throws SQLException {

		Connection c = UConexion.getInstanciaConexion();

		String tableName;

		Tabla table = o.getClass().getAnnotation(Tabla.class);

		if (table != null) {
			if (!table.nombre().replaceAll(" ", "").isEmpty()) {
				tableName = table.nombre().toLowerCase();
			} else {
				tableName = o.getClass().getSimpleName().toLowerCase();
			}
		} else {

			tableName = o.getClass().getSimpleName().toLowerCase();
		}

		ArrayList<Field> atts = UBean.obtenerAtributos(o);
		int cantidadFields = atts.size();

		String idColumnName = null;
		Object idValue = null;

		for (int i = 0; i < cantidadFields; i++) {

			Field att = atts.get(i);

			if (att.getAnnotation(Id.class) != null) {

				Columna column = att.getAnnotation(Columna.class);

				if (column != null) {

					if (!column.nombre().replaceAll(" ", "").isEmpty()) {
						idColumnName = column.nombre();
					} else {
						idColumnName = att.getName();
					}

					idValue = UBean.ejecutarGet(o, att.getName());

				} else {

					idColumnName = att.getName();
					idValue = UBean.ejecutarGet(o, att.getName());

				}

			}

		}

		String statement = "delete from " + tableName + " where " + idColumnName + " = ? ";

		PreparedStatement s = c.prepareStatement(statement);
		s.setObject(1, idValue);

		System.out.println(statement);

		s.execute();

		c.close();

	}

	public static Object obtenerPorId(Class c, Object id)
			throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {

		Connection con = UConexion.getInstanciaConexion();

		String tableName = null;
		String idColumnName = null;

		Tabla table = (Tabla) c.getAnnotation(Tabla.class);

		if (table != null) {
			if (!table.nombre().replaceAll(" ", "").isEmpty()) {
				tableName = table.nombre().toLowerCase();
			} else {
				tableName = c.getClass().getSimpleName().toLowerCase();
			}
		} else {

			tableName = c.getClass().getSimpleName().toLowerCase();
		}

		Object newObject = Class.forName(c.getName()).getConstructor().newInstance();

		ArrayList<Field> atts = UBean.obtenerAtributos(newObject);

		for (int i = 0; i < atts.size(); i++) {

			Field att = atts.get(i);

			if (att.getAnnotation(Id.class) != null) {

				Columna column = att.getAnnotation(Columna.class);

				if (column != null) {

					if (!column.nombre().replaceAll(" ", "").isEmpty()) {
						idColumnName = column.nombre();
					} else {
						idColumnName = att.getName();
					}

				} else {

					idColumnName = att.getName();

				}

				break;
			}

		}

		String statement = "select * from " + tableName + " where " + idColumnName + " = ? ";

		PreparedStatement s = con.prepareStatement(statement);
		s.setObject(1, id);

		ResultSet rs = s.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("per_nombre") + " " + rs.getString("dni"));

			for (int i = 0; i < atts.size(); i++) {

				Field att = atts.get(i);

				Columna column = att.getAnnotation(Columna.class);

				if (column != null) {
					String columnName;

					if (!column.nombre().replaceAll(" ", "").isEmpty()) {
						columnName = column.nombre();
					} else {
						columnName = att.getName();
					}
					
					UBean.ejecutarSet(newObject, att.getName() , rs.getObject(columnName));

				} else {

					// Si no tiene la anotacion columna devuelvo el nombre del campo
					
					Object value = rs.getObject(att.getName());

					UBean.ejecutarSet(newObject, att.getName() , value);
				}

			}

		}

		con.close();

		return newObject;

	}

	public static List<Object> obtenerTodos(Class c) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		
		Connection con = UConexion.getInstanciaConexion();

		String tableName = null;

		Tabla table = (Tabla) c.getAnnotation(Tabla.class);

		if (table != null) {
			if (!table.nombre().replaceAll(" ", "").isEmpty()) {
				tableName = table.nombre().toLowerCase();
			} else {
				tableName = c.getClass().getSimpleName().toLowerCase();
			}
		} else {

			tableName = c.getClass().getSimpleName().toLowerCase();
		}
		
		List<Object> newObjectList = new ArrayList<>();

		ArrayList<Field> atts = UBean.obtenerAtributos(Class.forName(c.getName()).getConstructor().newInstance());

		String statement = "select * from " + tableName ;

		PreparedStatement s = con.prepareStatement(statement);

		ResultSet rs = s.executeQuery();

		while (rs.next()) {
			
			Object newObject = Class.forName(c.getName()).getConstructor().newInstance();

			for (int i = 0; i < atts.size(); i++) {

				Field att = atts.get(i);

				Columna column = att.getAnnotation(Columna.class);

				if (column != null) {
					String columnName;

					if (!column.nombre().replaceAll(" ", "").isEmpty()) {
						columnName = column.nombre();
					} else {
						columnName = att.getName();
					}
					
					UBean.ejecutarSet(newObject, att.getName() , rs.getObject(columnName));

				} else {

					// Si no tiene la anotacion columna devuelvo el nombre del campo
					
					Object value = rs.getObject(att.getName());

					UBean.ejecutarSet(newObject, att.getName() , value);
				}

			}
			
			newObjectList.add(newObject);

		}

		con.close();

		return newObjectList;

	}

}
