package com.codingpower.loadstyle.fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * 自动进行状态维持的Fragment
 * @author pengf
 *
 */
public class StatusFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null)
		{
			restoreFromSavedInstanceState(savedInstanceState);
		}
	}
	
	//从值进行恢复
	protected void restoreFromSavedInstanceState(Bundle savedInstanceState)
	{
		if(savedInstanceState.keySet() != null)
		{
			for(String name : savedInstanceState.keySet())
			{
				Object val = savedInstanceState.get(name);
				try {
					//从整型值进行恢复
					Class paramClass = val.getClass();
					if(paramClass == ArrayList.class)
					{
						paramClass = List.class;
					}
					Method method = this.getClass().getMethod("set" + name,  paramClass);
					method.invoke(this, val);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//Map<String, Method> intStroeMap = new HashMap<String, Method>();
		//获取所有Parcelable和int型的属性
		//根据get方法获取值，进行持久化
		for(Method method : this.getClass().getMethods())
		{
			if(method.getName().startsWith("get"))
			{
				String name = method.getName().substring(3);
				Object val = null;
				try {
					val = method.invoke(this);
				} catch (Exception e) {
					continue;
				}
				
				if(val instanceof Integer)
				{
					Integer intVal = (Integer) val;
					if(intVal != null)
					{
						outState.putInt(name, intVal);
					}
				}
				else if(val instanceof  ArrayList)
				{
					ArrayList arrayVal = (ArrayList) val;
					if(arrayVal != null 
							&& !arrayVal.isEmpty()
							&& arrayVal.get(0) instanceof Parcelable)
					{
						outState.putParcelableArrayList(name, arrayVal);
					}
				}
			}
		}
	}
	
}
