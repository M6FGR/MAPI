//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package M6FGR.mapi.network.functions;
@FunctionalInterface
public interface StreamMemberDecoder<O, T> {
    T decode(T value, O output);
}
