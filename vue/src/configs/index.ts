import theme from "@/configs/theme";
import toolbar from "@/configs/toolbar";
import envs from "@/constants/envs";

export default {
  // product display information
  product: {
    name: envs.PRODUCT_TITLE,
    version: envs.PRODUCT_VERSION,
  },

  // theme configs
  theme,

  // toolbar configs
  toolbar,
};
