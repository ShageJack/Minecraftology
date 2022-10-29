package shagejack.industrimania.registries.block;

import net.minecraftforge.client.model.generators.ModelFile;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registries.block.grouped.AsBase;
import shagejack.industrimania.registries.dataGen.DataGenHandle;

import java.util.Objects;

import static shagejack.industrimania.registries.dataGen.DataGenHandle.checkTextureFileExist;

interface ModelBuilder extends AsBase{

    default BlockBuilder allSameModel(String path) {
        DataGenHandle.addBlockModelTask((provider) -> {
            var base = asBase();
            var block = base.block;
            Industrimania.LOGGER.debug("set cube all model for Block:{}", base.name);
            provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                    .parent(DataGenHandle.blockCubeAll.get()).texture("all", path);
        });
        return (BlockBuilder) this;
    }

    default BlockBuilder presetCropModel(int maxAge) {
        DataGenHandle.addBlockModelTask(provider -> {
            var base = asBase();
            var block = base.block;
            var category = Objects.requireNonNull(base.name).split("_")[0];
            var bName = Objects.requireNonNull(base.name).substring(category.length() + 1);
            for (int i = 0; i <= maxAge; i++) {
                final var path = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName) + "_stage" + i;
                Industrimania.LOGGER.debug("set preset model for Block:{}", base.name);
                provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_stage" + i)
                        .parent(DataGenHandle.modExistingModel(provider, path));
            }
        });
        return (BlockBuilder) this;
    }

    default BlockBuilder presetBinaryModel() {
        DataGenHandle.addBlockModelTask(provider -> {
            var base = asBase();
            var block = base.block;
            var category = Objects.requireNonNull(base.name).split("_")[0];
            var bName = Objects.requireNonNull(base.name).substring(category.length() + 1);
            final var pathTrue = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName);
            final var pathFalse = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName) + "_0";
            Industrimania.LOGGER.debug("set preset model for Block:{}", base.name);
            provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                    .parent(DataGenHandle.modExistingModel(provider, pathTrue));
            provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_0")
                    .parent(DataGenHandle.modExistingModel(provider, pathFalse));
        });
        return (BlockBuilder) this;
    }

    default BlockBuilder simplePresetModel() {
        DataGenHandle.addBlockModelTask(provider -> {
            var base = asBase();
            var block = base.block;
            var category = Objects.requireNonNull(base.name).split("_")[0];
            var bName = Objects.requireNonNull(base.name).substring(category.length() + 1);
            final var path = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName);
            Industrimania.LOGGER.debug("set preset model for Block:{}", base.name);
            provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                    .parent(DataGenHandle.modExistingModel(provider, path));
        });
        return (BlockBuilder) this;
    }

    default BlockBuilder allSpecificModel(String top, String bottom, String north, String south, String west, String east) {
        DataGenHandle.addBlockModelTask(provider -> {
            var base = asBase();
            var block = base.block;
            Industrimania.LOGGER.debug("""
                     set all specific model for Block:{} with
                      up:{}
                     down:{}
                     north:{}
                     south:{}
                     west:{}
                    east:{}""", base.name, top, bottom, north, south, west, east);
            provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                    .parent(DataGenHandle.blockCube.get())
                    .texture("up", top)
                    .texture("down", bottom)
                    .texture("north", north)
                    .texture("south", south)
                    .texture("west", west)
                    .texture("east", east);

        });
        return (BlockBuilder) this;
    }

    default BlockBuilder oreTextureModel() {
        DataGenHandle.addBlockModelTask(provider -> {
            var base = asBase();
            var name = base.name;
            try {
                var temp = Objects.requireNonNull(base.name).split("_");
                var rock = temp[1];
                var oreType = temp[2];
                var grade = temp[3];
                final var rockLayerPath = "block/rock/" + rock + "/default";
                final var oreLayerPath = "block/ore/layer/" + oreType + "/" + grade;


                if (checkTextureFileExist(provider, rockLayerPath) && checkTextureFileExist(provider, oreLayerPath)) {
                    Industrimania.LOGGER.debug("automatically set ore texture model for Block:{}", name);
                    provider.getBuilder(Objects.requireNonNull(base.block.get().getRegistryName()).getPath())
                            .parent(DataGenHandle.blockOre.get())
                            .texture("rock", rockLayerPath)
                            .texture("ore", oreLayerPath);
                } else {
                    Industrimania.LOGGER.debug("failed to set ore texture model for Block:{}, 'cause its texture doesn't exist.", name);
                }


            } catch (IllegalStateException e) {
                Industrimania.LOGGER.error("failed to set ore texture model for Ore:{},reason:{}", name, e.getMessage());
            }
        });
        return (BlockBuilder) this;
    }

    default BlockBuilder crossTextureModel() {
        DataGenHandle.addBlockModelTask(provider -> {
            var base = asBase();
            var name = base.name;
            var block = base.block;
            try {
                var category = Objects.requireNonNull(name).split("_")[0];
                var bName = Objects.requireNonNull(name).substring(category.length() + 1);
                final var path = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName) + "/cross";
                if (checkTextureFileExist(provider, path)) {
                    Industrimania.LOGGER.debug("automatically set cross texture model for Block:{}", name);
                    provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                            .parent(DataGenHandle.blockCrossTexture.get()).texture("cross", path);
                } else {
                    Industrimania.LOGGER.debug("failed to set cross texture model for Block:{}, 'cause its texture doesn't exist.", name);
                }
            } catch (IllegalStateException e) {
                Industrimania.LOGGER.error("failed to set cross texture model for Block:{},reason:{}", name, e.getMessage());
            }
        });
        return (BlockBuilder) this;
    }

    default BlockBuilder snowLikeModel() {
            DataGenHandle.addBlockModelTask(provider -> {
                var base = asBase();
                var name = base.name;
                var block = base.block;
                try {
                    var category = Objects.requireNonNull(name).split("_")[0];
                    var bName = Objects.requireNonNull(name).substring(category.length() + 1);
                    final var path = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName) + "/snowlike";
                    if (checkTextureFileExist(provider, path)) {
                        Industrimania.LOGGER.debug("automatically set snow like texture model for Block:{}", name);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_height2")
                                .parent(DataGenHandle.blockSnowLikeModel0.get()).texture("texture", path);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_height4")
                                .parent(DataGenHandle.blockSnowLikeModel1.get()).texture("texture", path);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_height6")
                                .parent(DataGenHandle.blockSnowLikeModel2.get()).texture("texture", path);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_height8")
                                .parent(DataGenHandle.blockSnowLikeModel3.get()).texture("texture", path);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_height10")
                                .parent(DataGenHandle.blockSnowLikeModel4.get()).texture("texture", path);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_height12")
                                .parent(DataGenHandle.blockSnowLikeModel5.get()).texture("texture", path);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath() + "_height14")
                                .parent(DataGenHandle.blockSnowLikeModel6.get()).texture("texture", path);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                                .parent(DataGenHandle.blockSnowLikeModel0.get()).texture("texture", path);
                    } else {
                        Industrimania.LOGGER.debug("failed to set snow like texture model for Block:{}, 'cause its texture doesn't exist.", name);
                    }
                } catch (IllegalStateException e) {
                    Industrimania.LOGGER.error("failed to set snow like texture model for Block:{},reason:{}", name, e.getMessage());
                }
            });
        return (BlockBuilder) this;
    }

    /**
     * The folder is assets/stagecraft/models/block/$BLOCK_NAME$/ <p>
     * while file names (ended with .png) are <p>
     * default texture if others do not exist <p>
     * particle <p> THIS IS FOR BLOCK PARTICLE
     * top <p>
     * bottom <p>
     * left <p>
     * right <p>
     * front <p>
     * back <p>
     * And the existence of the listing files means special occasions <p>
     * x #the front and the back are the same <p>
     * y #the left and the right are the same <p>
     * z #the bottom and the top are the same <p>
     * xyz supports combine
     * files specific less will override those which specifies less except the default
     * <p>
     * Argument : north is the front
     *
     * @return BlockBuilder
     */
    default BlockBuilder autoFullCubeModel(boolean hasRGBOverlay) {
        DataGenHandle.addBlockModelTask(provider -> {
            var base = asBase();
            var block = base.block;
            var name = base.name;
            try {
                var category = Objects.requireNonNull(name).split("_")[0];
                var bName = Objects.requireNonNull(name).substring(category.length() + 1);
                final var path = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName);
                if (checkTextureFileExist(provider, path)) {
                    Industrimania.LOGGER.debug("automatically set cube all model for Block:{}", name);
                    provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                            .parent(DataGenHandle.blockCubeAll.get()).texture("all", path);
                    return;
                }
                final var defaultTexture = String.format("%s/%s", path, "default");
                if (!checkTextureFileExist(provider, defaultTexture)) {
                    throw new IllegalStateException(String.format("can't find all same texture and default texture for Block:%s", name));
                }
                var front = defaultTexture;
                var back = defaultTexture;
                var left = defaultTexture;
                var right = defaultTexture;
                var top = defaultTexture;
                var bottom = defaultTexture;
                var particle = defaultTexture;

                var xyTexture = String.format("%s/xy", path);
                var xzTexture = String.format("%s/xy", path);
                var yzTexture = String.format("%s/yz", path);
                boolean xyExist = checkTextureFileExist(provider, xyTexture);
                boolean xzExist = checkTextureFileExist(provider, xzTexture);
                boolean yzExist = checkTextureFileExist(provider, yzTexture);
                checkThrow(xyExist && yzExist && xzExist, "xy,xz,yz");
                checkThrow(xyExist && xzExist, "xy,xz");
                checkThrow(xyExist && yzExist, "xy,yz");
                checkThrow(xzExist && yzExist, "xz,yz");
                if (xyExist) {
                    front = xyTexture;
                    back = xyTexture;
                    left = xyTexture;
                    right = xyTexture;
                }
                if (xzExist) {
                    front = xzTexture;
                    back = xzTexture;
                    top = xzTexture;
                    bottom = xzTexture;
                }
                if (yzExist) {
                    left = yzTexture;
                    right = yzTexture;
                    top = yzTexture;
                    bottom = yzTexture;
                }

                var xTexture = String.format("%s/x", path);
                var yTexture = String.format("%s/y", path);
                var zTexture = String.format("%s/z", path);
                boolean xExist = checkTextureFileExist(provider, xTexture);
                boolean yExist = checkTextureFileExist(provider, yTexture);
                boolean zExist = checkTextureFileExist(provider, zTexture);
                checkThrow(xyExist && xExist, "xy,x");
                checkThrow(xyExist && yExist, "xy,y");
                checkThrow(xzExist && xExist, "xz,x");
                checkThrow(xzExist && zExist, "xz,z");
                checkThrow(yzExist && yExist, "yz,y");
                checkThrow(yzExist && zExist, "yz,z");
                if (xExist) {
                    front = xTexture;
                    back = xTexture;
                }
                if (yExist) {
                    left = yTexture;
                    right = yTexture;
                }
                if (zExist) {
                    top = zTexture;
                    bottom = zTexture;
                }

                var frontTexture = String.format("%s/front", path);
                var backTexture = String.format("%s/back", path);
                var leftTexture = String.format("%s/left", path);
                var rightTexture = String.format("%s/right", path);
                var topTexture = String.format("%s/top", path);
                var bottomTexture = String.format("%s/bottom", path);
                var frontExist = checkTextureFileExist(provider, frontTexture);
                var backExist = checkTextureFileExist(provider, backTexture);
                var leftExist = checkTextureFileExist(provider, leftTexture);
                var rightExist = checkTextureFileExist(provider, rightTexture);
                var topExist = checkTextureFileExist(provider, topTexture);
                var bottomExist = checkTextureFileExist(provider, bottomTexture);
                checkThrow(xExist && frontExist && backExist, "x,front,back");
                checkThrow(xExist && frontExist, "x,front");
                checkThrow(xExist && backExist, "x,back");
                checkThrow(yExist && leftExist && rightExist, "y,left,right");
                checkThrow(yExist && leftExist, "y,left");
                checkThrow(yExist && rightExist, "y,right");
                checkThrow(zExist && topExist && bottomExist, "z,top,bottom");
                checkThrow(zExist && topExist, "z,top");
                checkThrow(zExist && bottomExist, "z,bottom");

                if (frontExist) {
                    front = frontTexture;
                }
                if (backExist) {
                    back = backTexture;
                }
                if (leftExist) {
                    left = leftTexture;
                }
                if (rightExist) {
                    right = rightTexture;
                }
                if (topExist) {
                    top = topTexture;
                }
                if (bottomExist) {
                    bottom = bottomTexture;
                }

                var particleTexture = String.format("%s/particle", path);
                var particleExist = checkTextureFileExist(provider, particleTexture);
                checkThrow(particleExist, "particle");

                if (particleExist) {
                    particle = particleTexture;
                }

                ModelFile model;

                if (hasRGBOverlay) {
                    model = DataGenHandle.blockCubeRGBOverlay.get();
                } else {
                    model = DataGenHandle.blockCube.get();
                }

                Industrimania.LOGGER.debug("""
                        automatically set full cube block model for Block:{} with
                        particle:{}
                        up:{}
                        down:{}
                        front/north:{}
                        back/south:{}
                        left/west:{}
                        right/east:{}
                        hasRGBOverlay:{}""", name, particle, top, bottom, front, back, left, right, hasRGBOverlay);
                provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                        .parent(model)
                        .texture("particle", particle)
                        .texture("up", top)
                        .texture("down", bottom)
                        .texture("north", front)
                        .texture("south", back)
                        .texture("west", left)
                        .texture("east", right);

            } catch (IllegalStateException e) {
                Industrimania.LOGGER.error("failed to set autoFullCubeModel for Block: {}, reason: {}", name, e.getMessage());
            }
        });
        return (BlockBuilder) this;
    }

    static void checkThrow(boolean condition, String message) {
        if (condition) {
            throw new IllegalStateException(String.format("%s specified at same at", message));
        }
    }

    default BlockBuilder autoFullCubeModel() {
        return this.autoFullCubeModel(false);
    }



}
