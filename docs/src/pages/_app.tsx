import type { AppProps } from "next/app";
import { Analytics } from "@vercel/analytics/react";

import "../style.css";

const MythicDropsDocs = ({ Component, pageProps }: AppProps) => (
  <>
    <Component {...pageProps} />
    <Analytics />
  </>
);

export default MythicDropsDocs;
