package demo;

import lombok.Data;

/**
 * @ClassName Node
 * @Description
 * @Author shuliyao
 * @CreateTime 2018/8/1 下午4:22
 */
@Data
public class Node {
    String value;
    Node node;
}
