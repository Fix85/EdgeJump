package dev.fix85.gracejump;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class GraceJumpModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.gracejump.config"));

            ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.gracejump.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(
                    Text.translatable("option.gracejump.enabled"),
                    Config.get().enabled
            )
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.gracejump.enabled.tooltip"))
                    .setSaveConsumer(newValue -> Config.get().enabled = newValue)
                    .build());

            general.addEntry(entryBuilder.startIntSlider(
                    Text.translatable("option.gracejump.ticks"),
                    Config.get().graceTicks,
                    1,
                    20
            )
                    .setDefaultValue(5)
                    .setTooltip(Text.translatable("option.gracejump.ticks.tooltip"))
                    .setSaveConsumer(newValue -> Config.get().graceTicks = newValue)
                    .build());

            builder.setSavingRunnable(Config::save);
            return builder.build();
        };
    }
}
