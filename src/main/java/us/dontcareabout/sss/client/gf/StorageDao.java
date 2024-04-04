package us.dontcareabout.sss.client.gf;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.storage.client.Storage;

//Refactory 移除 GST 版本的 StorageDao
//TODO 考慮增加可在 console 直接清空特定 key 的 js function
public class StorageDao<T> {
	private static Storage storage = Storage.getLocalStorageIfSupported();

	public final String key;

	private final ObjectMapper<T> mapper;

	public StorageDao(String key, ObjectMapper<T> mapper) {
		this.key = key;
		this.mapper = mapper;
	}

	/**
	 * @return 儲存在 local storage 的 instance
	 */
	public T retrieve() {
		String json = storage.getItem(key);
		return json == null ? null : mapper.read(json);
	}

	/**
	 * 將指定 list 儲存至 local storage
	 */
	public void store(T list) {
		storage.setItem(key, mapper.write(list));
	}

	/**
	 * 清空所有儲存在 local storage 的資料
	 */
	public void clear() {
		storage.removeItem(key);
	}
}
