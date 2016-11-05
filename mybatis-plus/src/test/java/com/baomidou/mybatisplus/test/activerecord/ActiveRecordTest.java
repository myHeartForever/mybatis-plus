/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.test.activerecord;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import com.baomidou.mybatisplus.MybatisSessionFactoryBuilder;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.test.mysql.TestMapper;
import com.baomidou.mybatisplus.test.mysql.entity.Test;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;

/**
 * <p>
 * ActiveRecord 测试
 * </p>
 *
 * @author Caratacus
 * @date 2016-10-11
 */
public class ActiveRecordTest {

	public static void main(String[] args) {
		// 加载配置文件
		InputStream in = TestMapper.class.getClassLoader().getResourceAsStream("mysql-config.xml");
		MybatisSessionFactoryBuilder mf = new MybatisSessionFactoryBuilder();
		SqlSessionFactory sqlSessionFactory = mf.build(in);
		TableInfoHelper.cacheSqlSessionFactory(sqlSessionFactory);

		// 保存一条记录
		Test t1 = new Test();
		t1.setType("test10");
		boolean rlt = t1.save();
		print(" ar save=" + rlt + ", id=" + t1.getId());

		// 根据ID更新
		t1.setType("t1023");
		rlt = t1.updateById();
		print(" ar updateById:" + rlt);

		// 更新 SQL
		rlt = t1.update("update test set type='123' where id=" + t1.getId());
		print("update sql=" + rlt);

		// 查询 SQL
		Test t10 = t1.selectOne("id=?", t1.getId());
		print("selectOne=" + t10.getType());

		// 插入OR更新
		t1.setType("t1021");
		rlt = t1.saveOrUpdate();
		print(" ar saveOrUpdate:" + rlt);

		// 根据ID查询
		Test t2 = t1.selectById();
		print(" t2 = " + t2.toString());
		t2.setId(IdWorker.getId());
		t2.save();

		// 查询所有
		List<Test> tl = t2.selectAll();
		for (Test t : tl) {
			print("selectAll=" + t.toString());
		}

		// 查询总记录数
		print(" count=" + t2.count());

		// 翻页查询
		Page<Test> page = new Page<Test>(0, 10);
		page = t2.Page(page);
		print(page.toString());

		// 根据ID删除
		rlt = t2.deleteById();
		print("deleteById=" + rlt + ", id=" + t2.getId());

		// 根据ID查询
		Test t20 = t2.selectById();
		print("t2 删除后是否存在？" + (null != t20));

		// 删除 SQL
		rlt = t2.delete("type=?", "t1021");
		System.err.println("delete sql=" + rlt);
	}

	/*
	 * 慢点打印
	 */
	private static void print(String text) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println(text);
	}

}
