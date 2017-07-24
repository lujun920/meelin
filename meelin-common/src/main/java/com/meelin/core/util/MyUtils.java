package com.meelin.core.util;

import com.meelin.core.exception.CoreBaseException;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.lang.reflect.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"rawtypes","unchecked"})
public abstract class MyUtils {
	public static final Pattern PROPERTY = Pattern.compile("([a-z])([A-Z])");
	public static final Pattern COLUMN = Pattern.compile("_([a-zA-Z])");
	public static final Pattern CHECK = Pattern.compile("[0-9A-Za-z]{2,64}?");
	public static final Pattern PLACEHOLDER = Pattern
			.compile("\\$\\{([\\w\\.\\[\\]]+)\\}");
	private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(
			8);

	static {
		primitiveWrapperTypeMap.put(Boolean.TYPE, Boolean.class);
		primitiveWrapperTypeMap.put(Byte.TYPE, Byte.class);
		primitiveWrapperTypeMap.put(Character.TYPE, Character.class);
		primitiveWrapperTypeMap.put(Double.TYPE, Double.class);
		primitiveWrapperTypeMap.put(Float.TYPE, Float.class);
		primitiveWrapperTypeMap.put(Integer.TYPE, Integer.class);
		primitiveWrapperTypeMap.put(Long.TYPE, Long.class);
		primitiveWrapperTypeMap.put(Short.TYPE, Short.class);
	}

	public static Class<?> getprimitiveWrapper(Class<?> key) {
		return (Class<?>) primitiveWrapperTypeMap.get(key);
	}

	public static <T> T getValue(T value, T dv) {
		return value == null ? dv : value;
	}

	public static <T extends Enum<T>> boolean isEnum(Class<T> clazz, String name) {
		try {
			Enum.valueOf(clazz, name);
			return true;
		} catch (Throwable localThrowable) {
		}
		return false;
	}

	public static <T> T getProperty(Object root, String property) {
		return null;
	}

	public static String getNextLevelCode(String parentCode, String maxLevelCode) {
		parentCode = (String) getValue(parentCode, "");
		maxLevelCode = (String) getValue(maxLevelCode, parentCode + "-1");
		Assert.isTrue(parentCode.length() % 2 == 0, "parentCode长度必须为偶数");
		Assert.isTrue(maxLevelCode.length() >= 2, "levelCode长度必须不小于2");
		Assert.isTrue(maxLevelCode.length() % 2 == 0, "levelCode长度必须为偶数");

		boolean lengthR = maxLevelCode.length() == parentCode.length() + 2;
		Assert.isTrue(lengthR, "levelCode长度=parentCode+2");
		String prefix = maxLevelCode.substring(0, parentCode.length());
		Assert.isTrue(prefix.equals(parentCode), "levelCode=parentCode##");

		Long nextNumber = Long.valueOf(new Long(maxLevelCode
				.substring(parentCode.length())).longValue() + 1L);
		String next = parentCode + new DecimalFormat("00").format(nextNumber);

		return next;
	}

	public static <T> T buildObject(Class<T> clazz) {
		Assert.notNull(clazz, "需要实例化的类对象不能为NULL");
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new CoreBaseException("实例化类对象异常", e);
		}
	}

	
	public static <T> T buildObject(Class<T> clazz, Object... args) {
		Assert.notNull(clazz, "需要实例化的类对象不能为NULL");
		Class[] argsTypes = new Class[args.length];
		for (int i = 0; i < argsTypes.length; i++) {
			argsTypes[i] = args[i].getClass();
		}
		try {
			Constructor<T> c = clazz.getConstructor(argsTypes);
			return c.newInstance(args);
		} catch (Exception e) {
			throw new CoreBaseException("实例化类对象异常", e);
		}
	}

	public static Class<?> forName(String className) {
		try {
			return ClassUtils
					.forName(className, MyUtils.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (LinkageError e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setFieldValue(Object target, String field, Object value) {
		Field fieldObject = ReflectionUtils.findField(target.getClass(), field);
		ReflectionUtils.makeAccessible(fieldObject);
		ReflectionUtils.setField(fieldObject, target, value);
	}

	public static Object getFieldValue(Object target, String field) {
		Field fieldObject = ReflectionUtils.findField(target.getClass(), field);
		ReflectionUtils.makeAccessible(fieldObject);
		return ReflectionUtils.getField(fieldObject, target);
	}

	public static List<String> arrayToList(String[] seq) {
		List<String> list = new ArrayList<String>(seq.length);
		String[] arrayOfString = seq;
		int j = seq.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString[i];
			list.add(s);
		}
		return list;
	}

	public static List<Map<String, Object>> ObjectToListMap(Object cllc,
			String... props) {
		List<?> list = Collections.EMPTY_LIST;
		if ((cllc instanceof Collection)) {
			list = (List<?>) cllc;
		} else if (cllc != null) {
			list = Arrays.asList(new Object[] { cllc });
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(list.size());
		for (Object object : list) {
			MetaObject metaObject = MetaObject.forObject(object,
					new DefaultObjectFactory(),
					new DefaultObjectWrapperFactory(),
					new DefaultReflectorFactory());

			Map<String, Object> map = new LinkedHashMap<String, Object>(props.length * 4 / 3);
			for (int i = 0; i < props.length; i++) {
				String prop = props[i];
				String[] pv = StringUtils.tokenizeToStringArray(prop, ":",
						true, true);
				if (pv.length == 1) {
					map.put(prop, metaObject.getValue(prop));
				} else {
					map.put(pv[0], metaObject.getValue(pv[1]));
				}
			}
			result.add(map);
		}
		return result;
	}

	public static List<String> CC(String seq, boolean set) {
		String[] strArray = StringUtils.commaDelimitedListToStringArray(seq);
		Collection<String> result = set ? new TreeSet() : new ArrayList(
				strArray.length);
		for (String str : strArray) {
			result.add(StringUtils.trimWhitespace(str));
		}
		return (List) (set ? new ArrayList(result) : result);
	}

	public static List<String> CC(String seq) {
		return CC(seq, false);
	}

	public static <K, V> List<V> CC(Map<K, ?> map, List<K> keys, String prop) {
		List<V> val = new ArrayList<V>(keys.size());
		for (K key : keys) {
			Object v = map.get(key);
			if ((StringUtils.hasText(prop)) && (v != null)) {
				v = MetaObject.forObject(v, new DefaultObjectFactory(),
						new DefaultObjectWrapperFactory(),
						new DefaultReflectorFactory()).getValue(prop);
			}
			if (v != null) {
				val.add((V) v);
			}
		}
		return val;
	}

	public static void copyProperties(Object from, Object to,
			Collection<String> propMapper) {
		MetaObject fromMeta = MetaObject.forObject(from,
				new DefaultObjectFactory(), new DefaultObjectWrapperFactory(),
				new DefaultReflectorFactory());

		MetaObject toMeta = MetaObject.forObject(to,
				new DefaultObjectFactory(), new DefaultObjectWrapperFactory(),
				new DefaultReflectorFactory());
		for (String mapper : propMapper) {
			String[] map = StringUtils.delimitedListToStringArray(mapper, ":");
			if (map.length == 1) {
				toMeta.setValue(map[0], fromMeta.getValue(map[0]));
			} else if (map.length == 2) {
				toMeta.setValue(map[1], fromMeta.getValue(map[0]));
			} else {
				throw new CoreBaseException("数据错误" + mapper);
			}
		}
	}

	public static <T> Class<T> getGenericClass(Class<?> genericClass) {
		return getGenericClass(genericClass, 0);
	}

	public static <T> Class<T> getGenericClass(Class<?> genericClass, int index) {
		Class<T> modelClass = null;
		TypeVariable[] tv = genericClass.getTypeParameters();
		if (tv.length == 0) {
			Type gsc = genericClass.getGenericSuperclass();
			if ((gsc instanceof ParameterizedType)) {
				ParameterizedType pt = (ParameterizedType) gsc;
				modelClass = (Class) pt.getActualTypeArguments()[index];
			} else {
				modelClass = getGenericClass((Class) gsc);
			}
		} else {
			modelClass = getGenericClass(genericClass, index);
		}
		return modelClass;
	}

	public static <T> Class<T> getGenericMethodReturnType(Class<?> clazz,
			String name, int index) {
		Method method = ReflectionUtils.findMethod(clazz, name);
		Type type = method.getGenericReturnType();
		if ((type instanceof ParameterizedType)) {
			Type[] types = ((ParameterizedType) type).getActualTypeArguments();
			return (Class) types[index];
		}
		throw new CoreBaseException("必须指定一个一级泛型方法");
	}

	public static <T> Class<T> getGenericMethodReturnType(Class<?> clazz,
			String name) {
		return getGenericMethodReturnType(clazz, name, 0);
	}

	public static String mapperColName(String propName) {
		return PROPERTY.matcher(propName).replaceAll("$1_$2").toUpperCase();
	}

//	public static String mapperPropName(String colName) {
//		return mapperPropName(colName, false);
//	}

//	public static String mapperPropName(String colName, boolean first) {
//		colName = colName.toLowerCase();
//		String[] names = colName.split("_");
//		StringBuilder buffer = new StringBuilder(colName.length());
//		for (int i = 0; i < names.length; i++) {
//			String frag = (i == 0) && (!first) ? names[i] : StringUtil
//					.capitalize(names[i]);
//			buffer.append(frag);
//		}
//		return buffer.toString();
//	}

	public static WebApplicationContext getAppCtx(Map<String, Object> app) {
		String appCtxKey = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
		return (WebApplicationContext) app.get(appCtxKey);
	}

	public static String removeBreakingWhitespace(String original) {
		StringTokenizer whitespaceStripper = new StringTokenizer(original);
		StringBuilder builder = new StringBuilder();
		while (whitespaceStripper.hasMoreTokens()) {
			builder.append(whitespaceStripper.nextToken());
			builder.append(" ");
		}
		return builder.toString();
	}

	public static Method findMethod(Class<?> clazz, String name,
			Class<?>... paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = searchType.isInterface() ? searchType
					.getMethods() : searchType.getDeclaredMethods();
			for (Method method : methods) {
				if ((name.equals(method.getName()))
						&& ((paramTypes == null) || (paramTypesCompatible(
								paramTypes, method.getParameterTypes())))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	private static boolean paramTypesCompatible(Class<?>[] tests,
			Class<?>[] candidates) {
		if (tests.length != candidates.length) {
			return false;
		}
		for (int i = 0; i < candidates.length; i++) {
			Class<?> candidate = candidates[i];
			Class<?> test = tests[i];
			if (!test.isAssignableFrom(candidate)) {
				return false;
			}
		}
		return true;
	}

	public static Method getMostSpecificMethod(Method method,
			Class<?> targetClass) {
		Method specificMethod = null;
		if ((method != null) && (isOverridable(method, targetClass))
				&& (targetClass != null)
				&& (!targetClass.equals(method.getDeclaringClass()))) {
			specificMethod = findMethod(targetClass, method.getName(),
					method.getParameterTypes());
		}
		return specificMethod != null ? specificMethod : method;
	}

	private static boolean isOverridable(Method method, Class<?> targetClass) {
		if (Modifier.isPrivate(method.getModifiers())) {
			return false;
		}
		if ((Modifier.isPublic(method.getModifiers()))
				|| (Modifier.isProtected(method.getModifiers()))) {
			return true;
		}
		return ClassUtils.getPackageName(method.getDeclaringClass()).equals(
				ClassUtils.getPackageName(targetClass));
	}

	public static String parserPlaceHolder(String process, Object ctx) {
		if (!StringUtils.hasText(process)) {
			return process;
		}
		Matcher matcher = PLACEHOLDER.matcher(process);
		MetaObject metaObject = MetaObject.forObject(ctx,
				new DefaultObjectFactory(), new DefaultObjectWrapperFactory(),
				new DefaultReflectorFactory());
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String p = matcher.group(1);
			if (metaObject.hasGetter(p)) {
				String replace = getValue(metaObject.getValue(p), "")
						.toString();
				matcher.appendReplacement(sb, replace);
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static <T> T deepClone(T source) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(source);
			oos.close();
			bos.close();

			ByteArrayInputStream bis = new ByteArrayInputStream(
					bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);
			Serializable result = (Serializable) ois.readObject();
			bis.close();
			ois.close();
			return (T) result;
		} catch (Exception e) {
			throw new CoreBaseException("对像深拷贝失败", e);
		}
	}

	public static <T> OCR<T> parserDiffer(Collection<T> older,
			Collection<T> newer) {
		return parserDiffer(older, newer, false);
	}

	public static <T> OCR<T> parserDiffer(Collection<T> older,
			Collection<T> newer, boolean update) {
		OCR<T> result = new OCR<T>();
		Set<T> olderSet = new LinkedHashSet<T>(older);
		Set<T> newerSet = new LinkedHashSet<T>(newer);
		for (T t : newerSet) {
			if (olderSet.contains(t)) {
				olderSet.remove(t);
				if (update) {
					result.getUpdate().add(t);
				}
			} else {
				result.getAdd().add(t);
			}
		}
		result.setDelete(new ArrayList<T>(olderSet));
		return result;
	}

	public static enum TM {
		SELF, LIST, MAP, GROUP, MAPLIST, JSON, SEQ;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if ((src == null) || (src.length <= 0)) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if ((hexString == null) || (hexString.equals(""))) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = ((byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[(pos + 1)])));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static class OCR<T> {
		List<T> add;
		List<T> delete;
		List<T> update;

		public List<T> getAdd() {
			if (this.add == null) {
				this.add = new ArrayList<T>();
			}
			return this.add;
		}

		public void setAdd(List<T> add) {
			this.add = add;
		}

		public List<T> getDelete() {
			if (this.delete == null) {
				this.delete = new ArrayList<T>();
			}
			return this.delete;
		}

		public void setDelete(List<T> delete) {
			this.delete = delete;
		}

		public List<T> getUpdate() {
			if (this.update == null) {
				this.update = new ArrayList<T>();
			}
			return this.update;
		}

		public void setUpdate(List<T> update) {
			this.update = update;
		}
	}
}
