import { defineConfig } from 'vite'
import { resolve } from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  base: '/lit',
  build: {
    outDir: 'src/main/resources/static/app',
    lib: {
      entry: 'frontend/src/main.ts',
      formats: ['es'],
    },
    manifest: true,
    rollupOptions: {
    },
  },
})
