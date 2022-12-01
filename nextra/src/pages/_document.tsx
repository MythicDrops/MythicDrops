import { Head, Html, Main, NextScript } from "next/document";
import { SkipNavLink } from "@reach/skip-nav";

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
