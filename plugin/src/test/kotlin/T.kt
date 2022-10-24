import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.Test
import top.e404.edungeongenerator.config.*

class T {
    private val yaml = Yaml(configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property))
    @Test
    fun t() {
        val mi = MiTreasure("miCategory", "miId", 10, 1..10)
        val mm = MmTreasure("mmId", 10, 1..10)
        val ia = IaTreasure("iaNamespaceId", 10, 1..10)
        val mc = McTreasure("mc", 10, 1..10)
        val list = listOf(mi, mm, ia, mc)
        val s = yaml.encodeToString(list)
        println(s)
        println(yaml.decodeFromString<List<Treasure>>(s))
    }
}