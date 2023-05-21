package be.minelabs.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.io.IOException;

public class DutchLangProvider extends MinelabsLangProvider {
    public DutchLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "nl_nl");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        super.generateTranslations(translationBuilder);
        //TODO COPY NL_BE
        try {
            translationBuilder.add(dataOutput.getModContainer()
                    .findPath("assets/minelabs/lang/nl_nl.json").get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}