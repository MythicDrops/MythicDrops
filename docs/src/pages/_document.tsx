import { Head, Html, Main, NextScript } from "next/document";
import { SkipNavLink } from "nextra-theme-docs";

const Document = () => (
  <Html lang="en">
    <Head />
    <body>
      <SkipNavLink />
      <Main />
      <NextScript />
    </body>
  </Html>
);

export default Document;
