package poker.gui;

/**
 * The purpose of IDeflatable is to remove Java dependencies on serialization.
 * All data that may be transmitted over the network must be deflatable. Any 
 * deflatable object, when deflated and inflated, should equal the original 
 * object when equals() is called.
 * @author kourge
 *
 * @param <T>
 */
public interface IDeflatable<T> {
	/**
	 * @return A serialized, stringified version of the object that can later 
	 * 			be inflated.
	 */
	public String deflate();
	/**
	 * @return A deserialized, inflated object constructed from the original 
	 * 			object. The inflated object should equal the original object 
	 * 			when equals() is called.
	 */
	public T inflate(String string) throws Exception;
}
