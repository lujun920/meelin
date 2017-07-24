package com.meelin.core.base.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.meelin.core.base.cnd.Cnd;
import com.meelin.core.base.cnd.ICmmAction;
import com.meelin.core.base.cnd.IIdNameStrategy;
import com.meelin.core.base.cnd.ISqlExp;
import com.meelin.core.base.cnd.SqlId;
import com.meelin.core.base.model.BaseModel;
import com.meelin.core.exception.CoreBaseException;
import com.meelin.core.exception.DaoException;
import com.meelin.core.pagehelper.Page;
import com.meelin.core.pagehelper.PageHelper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisDaoManager extends SqlSessionDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(MyBatisDaoManager.class);

	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	private boolean init = false;

	private IIdNameStrategy idNameStrategy;

	public SqlSession getSqlSession(BaseModel<?> baseModel) {
		ICmmAction<?> cmmAction = baseModel.getDataWrapper().getCmmAction();
		ICmmAction.ExeType exeType = cmmAction.getExeType();
//		TargetDataSource annotation= baseModel.getClass().getAnnotation(TargetDataSource.class);
//		if(null== annotation){
//			throw new CoreBaseException("Model未标识使用数据源!");
//		}
//		logger.info("used DataSource: {}", annotation.sessionFactory());
//
//		SqlSessionFactory targetSqlSessionFactory = (SqlSessionFactory)applicationContext.getBean(annotation.sessionFactory().toString());
//		setSqlSessionFactory(targetSqlSessionFactory);

		if ((exeType == null)
				|| (ICmmAction.ExeType.SIMPLE.equals(cmmAction.getExeType()))) {
			return getSqlSession();
		}
		throw new PersistanceException("不可能被执行的异常");
	}

	public static class PersistanceException extends CoreBaseException {
		private static final long serialVersionUID = 1L;
		public PersistanceException() {
		}
		public PersistanceException(String message, Throwable rootCause) {
			super(rootCause);
		}
		public PersistanceException(String message) {
			super();
		}
		public PersistanceException(Throwable rootCause) {
			super();
		}
	}

	public IIdNameStrategy getIdNameStrategy() {
		if (this.idNameStrategy == null) {
			this.idNameStrategy = new IIdNameStrategy.DefaultIdNameStrategy();
		}
		return this.idNameStrategy;
	}

	public void setIdNameStrategy(IIdNameStrategy idNameStrategy) {
		if (this.init) {
			throw new CoreBaseException("管理器已经初始化");
		}
		this.idNameStrategy = idNameStrategy;
	}

	/**
	 * 保存单条记录
	 * @param baseModel
	 * @return
	 */
	public Integer save(BaseModel<?> baseModel) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel, IIdNameStrategy.CRUD.save, cfg);
		return Integer.valueOf(sqlSession.insert(id, baseModel));
	}

	/**
	 * 删除单条记录
	 * @param baseModel
	 * @return
	 */
	public Integer remove(BaseModel<?> baseModel) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel,
				IIdNameStrategy.CRUD.remove, cfg);
		return Integer.valueOf(sqlSession.delete(id, baseModel));
	}

	/**
	 * 修改单条记录，不允许把记录修改为NULL
	 * @param baseModel
	 * @return
	 */
	public Integer update(BaseModel<?> baseModel) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel,
				IIdNameStrategy.CRUD.update, cfg);
		return Integer.valueOf(sqlSession.update(id, baseModel));
	}

	/**
	 * 强制修改单位记录，允许把记录修改为NULL
	 * @param baseModel
	 * @return
	 */
	public Integer foreUpdate(BaseModel<?> baseModel) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel,
				IIdNameStrategy.CRUD.forceUpdate, cfg);
		return Integer.valueOf(sqlSession.update(id, baseModel));
	}

	/**
	 * 获得单条记录信息（不包含父类信息）
	 * @param baseModel
	 * @return
	 */
	public <B extends BaseModel<B>> B load(B baseModel) {
		B fromDbModel = get(baseModel);
		String[] ignors = { "dataWrapper" };
		BeanUtils.copyProperties(fromDbModel, baseModel, ignors);
		return fromDbModel;
	}

	/**
	 * 获得单条记录信息（包含父类信息）
	 * @param baseModel
	 * @return
	 */
	public <B extends BaseModel<B>> B get(BaseModel<B> baseModel) {
		return get(baseModel, true);
	}

	/**
	 * 获得单条记录信息
	 * @param baseModel
	 * @param must
	 * @return
	 */
	public <B extends BaseModel<B>> B get(BaseModel<B> baseModel, boolean must) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel,
				IIdNameStrategy.CRUD.get, cfg);
		B fromDbModel = sqlSession.selectOne(id, baseModel);
		if ((fromDbModel == null) && (must)) {
			throw new PersistanceException("记录标识有误,或当前记录已经过时");
		}
		return fromDbModel;
	}

	/**
	 * 获得单条记录信息（必须确保查询的结果只有一条记录）
	 * @param baseModel
	 * @return
	 */
	public <B extends BaseModel<B>> B selectOne(BaseModel<B> baseModel) {
		return exeSelectOneCommand(baseModel, IIdNameStrategy.CRUD.selectModelList.getSqlId());
	}

	/**
	 * 获得单条记录信息（必须确保查询的结果只有一条记录）
	 * @param baseModel
	 * @param command
	 * @return
	 */
	public <T> T exeSelectOneCommand(BaseModel<?> baseModel, String command) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel, command, cfg);
		return sqlSession.selectOne(id, baseModel);
	}

	/**
	 * 自定义添加记录
	 * @param baseModel
	 * @param command
	 * @return
	 */
	public Integer exeInsertCommand(BaseModel<?> baseModel, String command) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel, command, cfg);
		return Integer.valueOf(sqlSession.insert(id, baseModel));
	}

	/**
	 * 自定义删除记录
	 * @param baseModel
	 * @param command
	 * @return
	 */
	public Integer exeDeleteCommand(BaseModel<?> baseModel, String command) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel, command, cfg);
		return Integer.valueOf(sqlSession.delete(id, baseModel));
	}

	/**
	 * 自定义修改记录
	 * @param baseModel
	 * @param command
	 * @return
	 */
	public Integer exeUpdateCommand(BaseModel<?> baseModel, String command) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel, command, cfg);
		return Integer.valueOf(sqlSession.update(id, baseModel));
	}

	/**
	 * 自定义查询，返回一个集合对象
	 * @param baseModel
	 * @param command
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> exeSelectCommand(BaseModel<?> baseModel, String command) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel, command, cfg);
		if(baseModel.getIsPage()){
			if (null == baseModel || null == baseModel.getStart() || null== baseModel.getLength()) {
				throw new DaoException("缺少分页所需参数!!!!");
			}
			Page<?> page = PageHelper.startPage(baseModel.getStart(), baseModel.getLength());
			List listMap = sqlSession.selectList(id, baseModel);
			baseModel.setStart(page.getStart());
			baseModel.setLength(page.getLength());
			Integer total = page.getRecordsTotal();
			baseModel.setRecordsTotal(total);
			baseModel.setRecordsFiltered(total);
//			baseModel.setData(listMap);
			return listMap;
		}else{
			return sqlSession.selectList(id, baseModel);
		}
	}

	/**
	 * 查询并返回一个Map类型的集合对象
	 * @param baseModel
	 * @return
	 */
	public <B extends BaseModel<B>> List<Map<String, Object>> selectMapList(
			BaseModel<B> baseModel) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel,
				IIdNameStrategy.CRUD.selectMapList, cfg);
		if(baseModel.getIsPage()){
			if (null == baseModel || null == baseModel.getStart() || null== baseModel.getLength()) {
				throw new DaoException("缺少分页所需参数!!!!");
			}
			Page<?> page = PageHelper.startPage(baseModel.getStart(), baseModel.getLength());
			List<Map<String, Object>> listMap = sqlSession.selectList(id, baseModel);
			baseModel.setStart(page.getStart());
			baseModel.setLength(page.getLength());
			Integer total = page.getRecordsTotal();
			baseModel.setRecordsTotal(total);
			baseModel.setRecordsFiltered(total);
			baseModel.setData(listMap);
			return listMap;
		}else{
			return sqlSession.selectList(id, baseModel);
		}
	}

	/**
	 * 查询并返回一个Model的集合对象
	 * @param baseModel
	 * @return
	 */
	public <B extends BaseModel<B>> List<B> selectModelList(
			BaseModel<B> baseModel) {
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel,
				IIdNameStrategy.CRUD.selectModelList, cfg);
		return sqlSession.selectList(id, baseModel);
	}

	public Integer saveBatch(List<?> listModel) throws DataAccessException {
		return saveBatch(listModel, SqlId.SQL_SAVE_BATCH + listModel.get(0).getClass().getSimpleName());
	}

	public Integer saveBatch(List<?> listModel, String mapperKey)
			throws DataAccessException {
		if (null == listModel || 0 == listModel.size()) {// 需要依赖于字段配置
			throw new DaoException("批量插入集合为空!!!!");
		}
		return getSqlSession().insert(mapperKey, listModel);
	}

	/**
	 * 批量删除（cnd对象里面必须拥有主键的一个集合）
	 * @param baseModel
	 * @return
	 */
	public Integer removeSome(BaseModel<?> baseModel) {
		if (baseModel.getCondition().isEmpty()) {
			throw new PersistanceException("不允许无条删除表中的记录");
		}
		if ((baseModel.getCondition() instanceof Cnd)) {
			((Cnd) baseModel.getCondition()).resetOrderBy();
		}
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel,
				IIdNameStrategy.CRUD.removeSome, cfg);
		return Integer.valueOf(sqlSession.delete(id, baseModel));
	}

	/**
	 * 批量修改
	 * @param baseModel
	 * @return
	 */
	public Integer updateSome(BaseModel<?> baseModel) {
		if (baseModel.getCondition().isEmpty()) {
			throw new PersistanceException("不允许无条更新表中的记录");
		}
		if (ISqlExp.EMPTY.equals(baseModel.getUpdateSet())) {
			throw new PersistanceException("必须指定更新的字段");
		}
		if ((baseModel.getCondition() instanceof Cnd)) {
			((Cnd) baseModel.getCondition()).resetOrderBy();
		}
		SqlSession sqlSession = getSqlSession(baseModel);
		Configuration cfg = sqlSession.getConfiguration();
		String id = getIdNameStrategy().getCommand(baseModel,
				IIdNameStrategy.CRUD.updateSome, cfg);
		return Integer.valueOf(sqlSession.update(id, baseModel));
	}

}