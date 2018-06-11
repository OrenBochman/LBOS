package concurrency.utils;


/** 
 * allow to pass everything by reference
 * 
 */
public class Ref<T>
{
    public T Value;

    public Ref(T value)
    {
        Value = value;
    }
}