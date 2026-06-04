/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      keyframes: {
        obFadeIn: {
          'from': { opacity: '0' },
          'to': { opacity: '1' },
        },
        obFadeOut: {
          'from': { opacity: '1' },
          'to': { opacity: '0' },
        },
        obSlideUp: {
          'from': { opacity: '0', transform: 'translateY(20px) scale(0.98)' },
          'to': { opacity: '1', transform: 'translateY(0) scale(1)' },
        },
        obSlideDown: {
          'from': { opacity: '1', transform: 'translateY(0) scale(1)' },
          'to': { opacity: '0', transform: 'translateY(15px) scale(0.98)' },
        },
      },
    },
  },
  plugins: [],
}