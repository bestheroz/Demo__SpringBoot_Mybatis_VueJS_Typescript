export function getRealHeightOfLayout(
  layout: {
    x: number;
    y: number;
    w: number;
    h: number;
    i: string;
  }[],
): number[] {
  if (layout.length === 1) {
    return layout.map((item) => item.h * 30 - 65);
  } else if (layout.length === 2) {
    return layout.map((item) => item.h * 30 - 92);
  } else {
    return [840];
  }
}
