import com.alibaba.fastjson.serializer.PropertyFilter;

//查询历史消息时，ChatMessage中的指定字段不序列化
public class HistoryMessagePropertyFilter implements PropertyFilter {

    @Override
    public boolean apply(Object object, String name, Object value) {
        if (name.equalsIgnoreCase("id")
                || name.equalsIgnoreCase("type")
                || name.equalsIgnoreCase("mine")) {
            //false表示last字段将被排除在外
            return false;
        }
        return true;
    }

}
