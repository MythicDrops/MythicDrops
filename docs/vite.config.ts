import fs from "fs";
import type {Plugin} from "vite";
import {defineConfig} from "vite";
import Components from "unplugin-vue-components/vite";
import Unocss from "unocss/vite";
import {presetAttributify, presetIcons, presetUno} from "unocss";
import {resolve} from "pathe";
import {VitePWA} from "vite-plugin-pwa";
import fg from "fast-glob";
import {
    githubusercontentRegex,
    mythicDropsDescription,
    mythicDropsName,
    mythicDropsShortName,
    pwaFontsRegex,
    pwaFontStylesRegex,
} from ".vitepress/meta";

export default defineConfig({
    ssr: {
        format: 'cjs',
    },
    legacy: {
        buildSsrCjsExternalHeuristics: true,
    },
    optimizeDeps: {
        // vitepress is aliased with replacement `join(DIST_CLIENT_PATH, '/index')`
        // This needs to be excluded from optimization
        exclude: ['vitepress'],
    },
    plugins: [
        Components({
            include: [/\.vue/, /\.md/],
            dirs: ".vitepress/components",
            dts: ".vitepress/components.d.ts",
        }),
        Unocss({
            shortcuts: [
                ['btn', 'px-4 py-1 rounded inline-flex justify-center gap-2 text-white leading-30px children:mya !no-underline cursor-pointer disabled:cursor-default disabled:bg-gray-600 disabled:opacity-50'],
            ],
            presets: [
                presetUno({
                    dark: 'media',
                }),
                presetAttributify(),
                presetIcons({
                    scale: 1.2,
                }),
            ],
        }),
        IncludesPlugin(),
        VitePWA({
            outDir: ".vitepress/dist",
            registerType: "autoUpdate",
            // include all static assets under public/
            includeAssets: fg.sync("**/*.{png,svg,ico,txt}", {
                cwd: resolve(__dirname, "public"),
            }),
            manifest: {
                id: "/",
                name: mythicDropsName,
                short_name: mythicDropsShortName,
                description: mythicDropsDescription,
                theme_color: "#ffffff",
                icons: [
                    {
                        src: "logo.svg",
                        sizes: "165x165",
                        type: "image/svg",
                        purpose: "any maskable",
                    },
                ],
            },
            workbox: {
                navigateFallbackDenylist: [/^\/new$/],
                globPatterns: ['**/*.{css,js,html,woff2}'],
                runtimeCaching: [
                    {
                        urlPattern: pwaFontsRegex,
                        handler: 'CacheFirst',
                        options: {
                            cacheName: 'google-fonts-cache',
                            expiration: {
                                maxEntries: 10,
                                maxAgeSeconds: 60 * 60 * 24 * 365, // <== 365 days
                            },
                            cacheableResponse: {
                                statuses: [0, 200],
                            },
                        },
                    },
                    {
                        urlPattern: pwaFontStylesRegex,
                        handler: 'CacheFirst',
                        options: {
                            cacheName: 'gstatic-fonts-cache',
                            expiration: {
                                maxEntries: 10,
                                maxAgeSeconds: 60 * 60 * 24 * 365, // <== 365 days
                            },
                            cacheableResponse: {
                                statuses: [0, 200],
                            },
                        },
                    },
                    {
                        urlPattern: githubusercontentRegex,
                        handler: 'CacheFirst',
                        options: {
                            cacheName: 'githubusercontent-images-cache',
                            expiration: {
                                maxEntries: 10,
                                maxAgeSeconds: 60 * 60 * 24 * 365, // <== 365 days
                            },
                            cacheableResponse: {
                                statuses: [0, 200],
                            },
                        },
                    },
                ],
            },
        }),
    ],
});

function IncludesPlugin(): Plugin {
    return {
        name: 'include-plugin',
        enforce: 'pre',
        transform(code, id) {
            let changed = false
            code = code.replace(/\[@@include\]\((.*?)\)/, (_, url) => {
                changed = true
                const full = resolve(id, url)
                return fs.readFileSync(full, 'utf-8')
            })
            if (changed)
                return code
        },
    }
}
