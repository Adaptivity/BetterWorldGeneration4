package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BWG4ChunkProviderGold implements IChunkProvider
{
    private Random rand;
    private BWG4NoiseOctavesBeta noiseGen1;
    private BWG4NoiseOctavesBeta noiseGen2;
    private BWG4NoiseOctavesBeta noiseGen3;
    private BWG4NoiseOctavesBeta noiseGen4;
    public BWG4NoiseOctavesBeta noiseGen5;
    public BWG4NoiseOctavesBeta noiseGen6;
    public BWG4NoiseOctavesBeta mobSpawnerNoise;

    private World worldObj;
    private final boolean mapFeaturesEnabled;
    private double[] noiseArray;
    private double[] stoneNoise = new double[256];
    private double sandNoise[] = new double[256];
    private double gravelNoise[] = new double[256];
    private MapGenBase caveGenerator = new MapGenCaves();
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();
    private MapGenVillage villageGenerator = new MapGenVillage();
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    private MapGenBase ravineGenerator = new MapGenRavine();
    private BiomeGenBase[] biomesForGeneration;

    double[] noise3;
    double[] noise1;
    double[] noise2;
    double[] noise5;
    double[] noise6;
    float[] parabolicField;
    int[][] field_73219_j = new int[32][32];

    public BWG4ChunkProviderGold(World par1World, long par2, boolean par4)
    {
        this.worldObj = par1World;
        this.mapFeaturesEnabled = par4;
        this.rand = new Random(par2);
        this.noiseGen1 = new BWG4NoiseOctavesBeta(this.rand, 16);
        this.noiseGen2 = new BWG4NoiseOctavesBeta(this.rand, 16);
        this.noiseGen3 = new BWG4NoiseOctavesBeta(this.rand, 8);
        this.noiseGen4 = new BWG4NoiseOctavesBeta(this.rand, 4);
        this.noiseGen5 = new BWG4NoiseOctavesBeta(this.rand, 10);
        this.noiseGen6 = new BWG4NoiseOctavesBeta(this.rand, 16);
        this.mobSpawnerNoise = new BWG4NoiseOctavesBeta(this.rand, 8);
    }

    /**
     * Generates the shape of the terrain for the chunk though its all stone though the water is frozen if the
     * temperature is low enough
     */
    public void generateTerrain(int par1, int par2, byte[] par3ArrayOfByte)
    {
        byte var4 = 4;
        byte var5 = 16;
        byte var6 = 63;
        int var7 = var4 + 1;
        byte var8 = 17;
        int var9 = var4 + 1;
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, par1 * 4 - 2, par2 * 4 - 2, var7 + 5, var9 + 5);
        this.noiseArray = this.initializeNoiseField(this.noiseArray, par1 * var4, 0, par2 * var4, var7, var8, var9);

        for (int var10 = 0; var10 < var4; ++var10)
        {
            for (int var11 = 0; var11 < var4; ++var11)
            {
                for (int var12 = 0; var12 < var5; ++var12)
                {
                    double var13 = 0.125D;
                    double var15 = this.noiseArray[((var10 + 0) * var9 + var11 + 0) * var8 + var12 + 0];
                    double var17 = this.noiseArray[((var10 + 0) * var9 + var11 + 1) * var8 + var12 + 0];
                    double var19 = this.noiseArray[((var10 + 1) * var9 + var11 + 0) * var8 + var12 + 0];
                    double var21 = this.noiseArray[((var10 + 1) * var9 + var11 + 1) * var8 + var12 + 0];
                    double var23 = (this.noiseArray[((var10 + 0) * var9 + var11 + 0) * var8 + var12 + 1] - var15) * var13;
                    double var25 = (this.noiseArray[((var10 + 0) * var9 + var11 + 1) * var8 + var12 + 1] - var17) * var13;
                    double var27 = (this.noiseArray[((var10 + 1) * var9 + var11 + 0) * var8 + var12 + 1] - var19) * var13;
                    double var29 = (this.noiseArray[((var10 + 1) * var9 + var11 + 1) * var8 + var12 + 1] - var21) * var13;

                    for (int var31 = 0; var31 < 8; ++var31)
                    {
                        double var32 = 0.25D;
                        double var34 = var15;
                        double var36 = var17;
                        double var38 = (var19 - var15) * var32;
                        double var40 = (var21 - var17) * var32;

                        for (int var42 = 0; var42 < 4; ++var42)
                        {
                            int var43 = var42 + var10 * 4 << 11 | 0 + var11 * 4 << 7 | var12 * 8 + var31;
                            short var44 = 128;
                            var43 -= var44;
                            double var45 = 0.25D;
                            double var49 = (var36 - var34) * var45;
                            double var47 = var34 - var49;

                            for (int var51 = 0; var51 < 4; ++var51)
                            {
                                if ((var47 += var49) > 0.0D)
                                {
                                    par3ArrayOfByte[var43 += var44] = (byte)Block.stone.blockID;
                                }
                                else if (var12 * 8 + var31 < var6)
                                {
                                    par3ArrayOfByte[var43 += var44] = (byte)Block.waterStill.blockID;
                                }
                                else
                                {
                                    par3ArrayOfByte[var43 += var44] = 0;
                                }
                            }

                            var34 += var38;
                            var36 += var40;
                        }

                        var15 += var23;
                        var17 += var25;
                        var19 += var27;
                        var21 += var29;
                    }
                }
            }
        }
    }

    /**
     * Replaces the stone that was placed in with blocks that match the biome
     */
    public void replaceBlocksForBiome(int par1, int par2, byte[] par3ArrayOfByte, BiomeGenBase[] par4ArrayOfBiomeGenBase)
    {
        byte var5 = 63;
        double var6 = 0.03125D;
        stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, par1 * 16, par2 * 16, 0, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);
        sandNoise = noiseGen4.generateNoiseOctaves(sandNoise, par1 * 16, par2 * 16, 0.0D, 16, 16, 1, var6, var6, 1.0D);
        gravelNoise = noiseGen4.generateNoiseOctaves(gravelNoise, par1 * 16, 109.0134D, par2 * 16, 16, 1, 16, var6, 1.0D, var6);

        for (int var8 = 0; var8 < 16; ++var8)
        {
            for (int var9 = 0; var9 < 16; ++var9)
            {
                BiomeGenBase var10 = par4ArrayOfBiomeGenBase[var9 + var8 * 16];
                boolean flag = sandNoise[var8 + var9 * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean flag1 = gravelNoise[var8 + var9 * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                float var11 = var10.getFloatTemperature();
                int var12 = (int)(this.stoneNoise[var8 + var9 * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
                int var13 = -1;
				int vulcano = -1;
                byte var14 = var10.topBlock;
                byte var15 = var10.fillerBlock;

                for (int var16 = 127; var16 >= 0; --var16)
                {
                    int var17 = (var9 * 16 + var8) * 128 + var16;

                    if (var16 <= 0 + this.rand.nextInt(5))
                    {
                        par3ArrayOfByte[var17] = (byte)Block.bedrock.blockID;
                    }
                    else
                    {
                        byte var18 = par3ArrayOfByte[var17];

                        if (var18 == 0)
                        {
                            var13 = -1;
                        }
                        else if (var18 == Block.stone.blockID)
                        {
							if(var10.topBlock == Block.obsidian.blockID) //Vulcano code
							{
								if(var16 == 127) { vulcano = 10; par3ArrayOfByte[var17] = 0; }
								else if(vulcano > -1) {
									if(vulcano == 10) { par3ArrayOfByte[var17] = 0; vulcano--; }
									else if(vulcano == 9) { par3ArrayOfByte[var17] = 0; vulcano--; }
									else if(vulcano == 8) { par3ArrayOfByte[var17] = (byte)Block.lavaMoving.blockID; vulcano--; }
									else if(vulcano > -1) { par3ArrayOfByte[var17] = (byte)Block.lavaStill.blockID; vulcano--; } }	
								else { par3ArrayOfByte[var17] = (byte)Block.obsidian.blockID; }
							}
                            else if (var13 == -1)
                            {
								var14 = var10.topBlock;
								var15 = var10.fillerBlock;
							
                                if (var12 <= 0)
                                {
                                    var14 = 0;
                                    var15 = (byte)Block.stone.blockID;
                                }
								
								if(var10.mountainStart != 128)
								{
									if(var16 == var10.mountainStart    ) { if(rand.nextInt(4) == 0) { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 == var10.mountainStart + 1) { if(rand.nextInt(3) == 0) { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 == var10.mountainStart + 2) { if(rand.nextInt(2) == 0) { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 == var10.mountainStart + 3) { if(rand.nextInt(3) != 0) { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 == var10.mountainStart + 4) { if(rand.nextInt(5) != 0) { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var10.mountainSnow == true)
									{
										if(var16 > var10.mountainStart + 10) { var14 = (byte)Block.blockSnow.blockID; var15 = (byte)Block.blockSnow.blockID; }
										else if(var16 == var10.mountainStart + 10) { if(rand.nextInt(5) != 0) { var14 = (byte)Block.blockSnow.blockID; var15 = (byte)Block.blockSnow.blockID; } else { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } }
										else if(var16 == var10.mountainStart + 9) { if(rand.nextInt(4) != 0) { var14 = (byte)Block.blockSnow.blockID; var15 = (byte)Block.blockSnow.blockID; } else { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } }
										else if(var16 == var10.mountainStart + 8) { if(rand.nextInt(3) != 0) { var14 = (byte)Block.blockSnow.blockID; var15 = (byte)Block.blockSnow.blockID; } else { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } }
										else if(var16 == var10.mountainStart + 7) { if(rand.nextInt(2) == 0) { var14 = (byte)Block.blockSnow.blockID; var15 = (byte)Block.stone.blockID; } else { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } }
										else if(var16 == var10.mountainStart + 6) { if(rand.nextInt(3) == 0) { var14 = (byte)Block.blockSnow.blockID; var15 = (byte)Block.stone.blockID; } else { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } }
										else if(var16 == var10.mountainStart + 5) { if(rand.nextInt(4) == 0) { var14 = (byte)Block.blockSnow.blockID; var15 = (byte)Block.stone.blockID; } else { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; } }
									}
									else
									{
										if(var16 > var10.mountainStart + 4) { var14 = (byte)Block.stone.blockID; var15 = (byte)Block.stone.blockID; }
									}
								}

								if(var10.isCanyon)
								{
									if(var16 == 85) { if(rand.nextInt(4) == 0) { var14 = (byte)Block.sand.blockID; var15 = (byte)Block.sand.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 == 86) { if(rand.nextInt(3) == 0) { var14 = (byte)Block.sand.blockID; var15 = (byte)Block.sand.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 == 87) { if(rand.nextInt(2) == 0) { var14 = (byte)Block.sand.blockID; var15 = (byte)Block.sand.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 == 88) { if(rand.nextInt(3) != 0) { var14 = (byte)Block.sand.blockID; var15 = (byte)Block.sand.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 == 89) { if(rand.nextInt(5) != 0) { var14 = (byte)Block.sand.blockID; var15 = (byte)Block.sand.blockID; } else { var14 = var10.topBlock; var15 = var10.fillerBlock; } }
									else if(var16 > 89) { var14 = (byte)Block.sand.blockID; var15 = (byte)Block.sand.blockID; }
								}
								
								if(var10.beachID > 0 && var16 >= var5 - 10 && var16 <= var5 + 1)
								{
									if (var10.beachID == 1)
									{
										var14 = var10.topBlock;
										var15 = var10.fillerBlock;
										if(flag1)
										{
											var14 = 0;
											var15 = (byte)Block.gravel.blockID;
										}
										if(flag)
										{
											var14 = (byte)Block.sand.blockID;
											var15 = (byte)Block.sand.blockID;
										}
									}
									if(var10.beachID == 2)
									{
										var14 = (byte)Block.sand.blockID;
										var15 = (byte)Block.sand.blockID;
									}
									if(var10.beachID == 3)
									{
										var14 = 0;
										var15 = (byte)Block.gravel.blockID;
									}
								}	
								
								if(var10.randblock != 0)
								{
									if(rand.nextInt(var10.randblock) == 0)
									{
										var14 = var10.topBlock;
										var15 = var10.fillerBlock;
									}
									else
									{
										var14 = var10.topBlock2;
										var15 = var10.fillerBlock2;
									}	
								}

                                if (var16 < var5 && var14 == 0)
                                {
                                    if (var11 < 0.15F)
                                    {
                                        var14 = (byte)Block.ice.blockID;
                                    }
                                    else
                                    {
                                        var14 = (byte)Block.waterStill.blockID;
                                    }
                                }
								
                                var13 = var12;

                                if (var16 >= var5 - 1)
                                {
                                    par3ArrayOfByte[var17] = var14;
                                }
                                else
                                {
                                    par3ArrayOfByte[var17] = var15;
                                }
                            }
                            else if (var13 > 0)
                            {
                                --var13;
                                par3ArrayOfByte[var17] = var15;

                                if (var13 == 0 && var15 == Block.sand.blockID)
                                {
									var13 = this.rand.nextInt(4);
									if(var10.isCanyon)
									{
										var13 += 4;
									}
                                    var15 = (byte)Block.sandStone.blockID;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int par1, int par2)
    {
        return this.provideChunk(par1, par2);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int par1, int par2)
    {
        this.rand.setSeed((long)par1 * 341873128712L + (long)par2 * 132897987541L);
        byte[] var3 = new byte[32768];
        this.generateTerrain(par1, par2, var3);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
        this.replaceBlocksForBiome(par1, par2, var3, this.biomesForGeneration);
        this.caveGenerator.generate(this, this.worldObj, par1, par2, var3);
        this.ravineGenerator.generate(this, this.worldObj, par1, par2, var3);

        if (this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.generate(this, this.worldObj, par1, par2, var3);
            this.villageGenerator.generate(this, this.worldObj, par1, par2, var3);
            this.strongholdGenerator.generate(this, this.worldObj, par1, par2, var3);
            this.scatteredFeatureGenerator.generate(this, this.worldObj, par1, par2, var3);
        }

        Chunk var4 = new Chunk(this.worldObj, var3, par1, par2);
        byte[] var5 = var4.getBiomeArray();

        for (int var6 = 0; var6 < var5.length; ++var6)
        {
            var5[var6] = (byte)this.biomesForGeneration[var6].biomeID;
        }

        var4.generateSkylightMap();
        return var4;
    }

    /**
     * generates a subset of the level's terrain data. Takes 7 arguments: the [empty] noise array, the position, and the
     * size.
     */
    private double[] initializeNoiseField(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        if (par1ArrayOfDouble == null)
        {
            par1ArrayOfDouble = new double[par5 * par6 * par7];
        }

        if (this.parabolicField == null)
        {
            this.parabolicField = new float[25];

            for (int var8 = -2; var8 <= 2; ++var8)
            {
                for (int var9 = -2; var9 <= 2; ++var9)
                {
                    float var10 = 10.0F / MathHelper.sqrt_float((float)(var8 * var8 + var9 * var9) + 0.2F);
                    this.parabolicField[var8 + 2 + (var9 + 2) * 5] = var10;
                }
            }
        }

        double var44 = 684.412D;
        double var45 = 684.412D;
        this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, par2, par4, par5, par7, 1.121D, 1.121D, 0.5D);
        this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, par2, par4, par5, par7, 200.0D, 200.0D, 0.5D);
        this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, par2, par3, par4, par5, par6, par7, var44 / 80.0D, var45 / 160.0D, var44 / 80.0D);
        this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, par2, par3, par4, par5, par6, par7, var44, var45, var44);
        this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, par2, par3, par4, par5, par6, par7, var44, var45, var44);
        boolean var43 = false;
        boolean var42 = false;
        int var12 = 0;
        int var13 = 0;

        for (int var14 = 0; var14 < par5; ++var14)
        {
            for (int var15 = 0; var15 < par7; ++var15)
            {
                float var16 = 0.0F;
                float var17 = 0.0F;
                float var18 = 0.0F;
                byte var19 = 2;
                BiomeGenBase var20 = this.biomesForGeneration[var14 + 2 + (var15 + 2) * (par5 + 5)];

                for (int var21 = -var19; var21 <= var19; ++var21)
                {
                    for (int var22 = -var19; var22 <= var19; ++var22)
                    {
                        BiomeGenBase var23 = this.biomesForGeneration[var14 + var21 + 2 + (var15 + var22 + 2) * (par5 + 5)];
                        float var24 = this.parabolicField[var21 + 2 + (var22 + 2) * 5] / (var23.minHeight + 2.0F);

                        if (var23.minHeight > var20.minHeight)
                        {
                            var24 /= 2.0F;
                        }

                        var16 += var23.maxHeight * var24;
                        var17 += var23.minHeight * var24;
                        var18 += var24;
                    }
                }

                var16 /= var18;
                var17 /= var18;
                var16 = var16 * 0.9F + 0.1F;
                var17 = (var17 * 4.0F - 1.0F) / 8.0F;
                double var47 = this.noise6[var13] / 8000.0D;

                if (var47 < 0.0D)
                {
                    var47 = -var47 * 0.3D;
                }

                var47 = var47 * 3.0D - 2.0D;

                if (var47 < 0.0D)
                {
                    var47 /= 2.0D;

                    if (var47 < -1.0D)
                    {
                        var47 = -1.0D;
                    }

                    var47 /= 1.4D;
                    var47 /= 2.0D;
                }
                else
                {
                    if (var47 > 1.0D)
                    {
                        var47 = 1.0D;
                    }

                    var47 /= 8.0D;
                }

                ++var13;

                for (int var46 = 0; var46 < par6; ++var46)
                {
                    double var48 = (double)var17;
                    double var26 = (double)var16;
                    var48 += var47 * 0.2D;
                    var48 = var48 * (double)par6 / 16.0D;
                    double var28 = (double)par6 / 2.0D + var48 * 4.0D;
                    double var30 = 0.0D;
                    double var32 = ((double)var46 - var28) * 12.0D * 128.0D / 128.0D / var26;

                    if (var32 < 0.0D)
                    {
                        var32 *= 4.0D;
                    }

                    double var34 = this.noise1[var12] / 512.0D;
                    double var36 = this.noise2[var12] / 512.0D;
                    double var38 = (this.noise3[var12] / 10.0D + 1.0D) / 2.0D;

                    if (var38 < 0.0D)
                    {
                        var30 = var34;
                    }
                    else if (var38 > 1.0D)
                    {
                        var30 = var36;
                    }
                    else
                    {
                        var30 = var34 + (var36 - var34) * var38;
                    }

                    var30 -= var32;

                    if (var46 > par6 - 4)
                    {
                        double var40 = (double)((float)(var46 - (par6 - 4)) / 3.0F);
                        var30 = var30 * (1.0D - var40) + -10.0D * var40;
                    }

                    par1ArrayOfDouble[var12] = var30;
                    ++var12;
                }
            }
        }

        return par1ArrayOfDouble;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int par1, int par2)
    {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
    {
        BlockSand.fallInstantly = true;
        int var4 = par2 * 16;
        int var5 = par3 * 16;
        BiomeGenBase var6 = this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        long var7 = this.rand.nextLong() / 2L * 2L + 1L;
        long var9 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)par2 * var7 + (long)par3 * var9 ^ this.worldObj.getSeed());
        boolean var11 = false;

        if (this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
            var11 = this.villageGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
            this.strongholdGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
            this.scatteredFeatureGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
        }

        int var12;
        int var13;
        int var14;
		
		//RUINES/STRUCTURES
		/*
		if(var6 == BiomeGenBase.GOLD3tropicIsland || var6 == BiomeGenBase.GOLD3tropicBeach )
		{
			if (rand.nextInt(30) == 0)
			{
				int statue1 = var4 + rand.nextInt(16) + 8;
				int statue2 = var5 + rand.nextInt(16) + 8;
				if(worldObj.getHeightValue(statue1, statue2) < 75)
				{
					(new BWG4decoGold3(1)).generate(worldObj, rand, statue1, worldObj.getHeightValue(statue1, statue2), statue2);
				}	
			}
		}
		*/
		//for (var12 = 0; var12 < 2; ++var12)
		//{
		//	int snowX = var4 + rand.nextInt(16) + 8;
		//	int snowY = var5 + rand.nextInt(16) + 8;
		//	(new BWG4decoGold1(4)).generate(worldObj, rand, snowX, 62, snowY);
		//}

		if(var6.lakesDisabled)
		{
			if (!var11 && this.rand.nextInt(10) == 0)
			{
				var12 = var4 + this.rand.nextInt(16) + 8;
				var13 = this.rand.nextInt(50);
				var14 = var5 + this.rand.nextInt(16) + 8;
				(new WorldGenLakes(Block.waterStill.blockID)).generate(this.worldObj, this.rand, var12, var13, var14);
			}
			if (!var11 && this.rand.nextInt(8) == 0)
			{
				var12 = var4 + this.rand.nextInt(16) + 8;
				var13 = this.rand.nextInt(this.rand.nextInt(120) + 8);
				var14 = var5 + this.rand.nextInt(16) + 8;

				if (var13 < 50 || this.rand.nextInt(10) == 0)
				{
					(new WorldGenLakes(Block.lavaStill.blockID)).generate(this.worldObj, this.rand, var12, var13, var14);
				}
			}
		}
		else
		{
			if (!var11 && this.rand.nextInt(4) == 0)
			{
				var12 = var4 + this.rand.nextInt(16) + 8;
				var13 = this.rand.nextInt(128);
				var14 = var5 + this.rand.nextInt(16) + 8;
				(new WorldGenLakes(Block.waterStill.blockID)).generate(this.worldObj, this.rand, var12, var13, var14);
			}
			if (!var11 && this.rand.nextInt(8) == 0)
			{
				var12 = var4 + this.rand.nextInt(16) + 8;
				var13 = this.rand.nextInt(this.rand.nextInt(120) + 8);
				var14 = var5 + this.rand.nextInt(16) + 8;

				if (var13 < 63 || this.rand.nextInt(10) == 0)
				{
					(new WorldGenLakes(Block.lavaStill.blockID)).generate(this.worldObj, this.rand, var12, var13, var14);
				}
			}
		}	

        for(int i2 = 0; i2 < 20; i2++)
        {
            int k5 = var4 + rand.nextInt(16);
            int l8 = rand.nextInt(128);
            int k11 = var5 + rand.nextInt(16);
            (new BWG4oldGenClay(32, 2)).generate(worldObj, rand, k5, l8, k11);
        }				
		
        for (var12 = 0; var12 < 8; ++var12)
        {
            var13 = var4 + this.rand.nextInt(16) + 8;
            var14 = this.rand.nextInt(128);
            int var15 = var5 + this.rand.nextInt(16) + 8;

            if ((new BWG4decoDungeons(7, true, false, false)).generate(this.worldObj, this.rand, var13, var14, var15))
            {
                ;
            }
        }

        var6.decorate(this.worldObj, this.rand, var4, var5);
        SpawnerAnimals.performWorldGenSpawning(this.worldObj, var6, var4 + 8, var5 + 8, 16, 16, this.rand);
        var4 += 8;
        var5 += 8;

        for (var12 = 0; var12 < 16; ++var12)
        {
            for (var13 = 0; var13 < 16; ++var13)
            {
                var14 = this.worldObj.getPrecipitationHeight(var4 + var12, var5 + var13);
				
				if(var14 > var6.snowLevel)
				{
					if (this.worldObj.isBlockFreezable(var12 + var4, var14 - 1, var13 + var5))
					{
						this.worldObj.setBlockAndMetadataWithNotify(var12 + var4, var14 - 1, var13 + var5, Block.ice.blockID, 0, 2);
					}

					if (this.worldObj.canSnowAt(var12 + var4, var14, var13 + var5))
					{
						this.worldObj.setBlockAndMetadataWithNotify(var12 + var4, var14, var13 + var5, Block.snow.blockID, 0, 2);
					}
				}	
            }
        }

        BlockSand.fallInstantly = false;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        return true;
    }
	
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    /**
     * Unloads the 100 oldest chunks from memory, due to a bug with chunkSet.add() never being called it thinks the list
     * is always empty and will not remove any chunks.
     */
    public boolean unload100OldestChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "RandomLevelSource";
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
        BiomeGenBase var5 = this.worldObj.getBiomeGenForCoords(par2, par4);
        return var5 == null ? null : (var5 == BiomeGenBase.swampland && par1EnumCreatureType == EnumCreatureType.monster && this.scatteredFeatureGenerator.hasStructureAt(par2, par3, par4) ? this.scatteredFeatureGenerator.getScatteredFeatureSpawnList() : var5.getSpawnableList(par1EnumCreatureType));
    }

    /**
     * Returns the location of the closest structure of the specified type. If not found returns null.
     */
    public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5)
    {
        return "Stronghold".equals(par2Str) && this.strongholdGenerator != null ? this.strongholdGenerator.getNearestInstance(par1World, par3, par4, par5) : null;
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    public void recreateStructures(int par1, int par2)
    {
        if (this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.generate(this, this.worldObj, par1, par2, (byte[])null);
            this.villageGenerator.generate(this, this.worldObj, par1, par2, (byte[])null);
            this.strongholdGenerator.generate(this, this.worldObj, par1, par2, (byte[])null);
            this.scatteredFeatureGenerator.generate(this, this.worldObj, par1, par2, (byte[])null);
        }
    }
}