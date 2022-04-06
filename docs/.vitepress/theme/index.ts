import "uno.css";
import { inBrowser } from "vitepress";
import DefaultTheme from "vitepress/theme";

if (inBrowser) {
  import("./pwa");
}

export default DefaultTheme;
