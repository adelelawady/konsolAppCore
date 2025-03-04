/*!
 *
 * plugins/mobilefriendly.js
 * Litepicker v2.0.12 (https://github.com/wakirin/Litepicker)
 * Package: litepicker (https://www.npmjs.com/package/litepicker)
 * License: MIT (https://github.com/wakirin/Litepicker/blob/master/LICENCE.md)
 * Copyright 2019-2021 Rinat G.
 *
 * Hash: fc3887e0bb19d54c36db
 *
 */ !(function (e) {
  var t = {};
  function n(o) {
    if (t[o]) return t[o].exports;
    var r = (t[o] = { i: o, l: !1, exports: {} });
    return e[o].call(r.exports, r, r.exports, n), (r.l = !0), r.exports;
  }
  (n.m = e),
    (n.c = t),
    (n.d = function (e, t, o) {
      n.o(e, t) || Object.defineProperty(e, t, { enumerable: !0, get: o });
    }),
    (n.r = function (e) {
      'undefined' != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, { value: 'Module' }),
        Object.defineProperty(e, '__esModule', { value: !0 });
    }),
    (n.t = function (e, t) {
      if ((1 & t && (e = n(e)), 8 & t)) return e;
      if (4 & t && 'object' == typeof e && e && e.__esModule) return e;
      var o = Object.create(null);
      if ((n.r(o), Object.defineProperty(o, 'default', { enumerable: !0, value: e }), 2 & t && 'string' != typeof e))
        for (var r in e)
          n.d(
            o,
            r,
            function (t) {
              return e[t];
            }.bind(null, r)
          );
      return o;
    }),
    (n.n = function (e) {
      var t =
        e && e.__esModule
          ? function () {
              return e.default;
            }
          : function () {
              return e;
            };
      return n.d(t, 'a', t), t;
    }),
    (n.o = function (e, t) {
      return Object.prototype.hasOwnProperty.call(e, t);
    }),
    (n.p = ''),
    n((n.s = 2));
})([
  ,
  ,
  function (e, t, n) {
    'use strict';
    n.r(t);
    n(3);
    function o(e, t) {
      var n = Object.keys(e);
      if (Object.getOwnPropertySymbols) {
        var o = Object.getOwnPropertySymbols(e);
        t &&
          (o = o.filter(function (t) {
            return Object.getOwnPropertyDescriptor(e, t).enumerable;
          })),
          n.push.apply(n, o);
      }
      return n;
    }
    function r(e) {
      for (var t = 1; t < arguments.length; t++) {
        var n = null != arguments[t] ? arguments[t] : {};
        t % 2
          ? o(Object(n), !0).forEach(function (t) {
              i(e, t, n[t]);
            })
          : Object.getOwnPropertyDescriptors
          ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(n))
          : o(Object(n)).forEach(function (t) {
              Object.defineProperty(e, t, Object.getOwnPropertyDescriptor(n, t));
            });
      }
      return e;
    }
    function i(e, t, n) {
      return t in e ? Object.defineProperty(e, t, { value: n, enumerable: !0, configurable: !0, writable: !0 }) : (e[t] = n), e;
    }
    Litepicker.add('mobilefriendly', {
      init: function (e) {
        var t = e.options;
        (e.options.mobilefriendly = r(r({}, { breakpoint: 480 }), t.mobilefriendly)),
          Object.defineProperties(e, {
            xTouchDown: { value: null, writable: !0 },
            yTouchDown: { value: null, writable: !0 },
            touchTargetMonth: { value: null, writable: !0 },
          });
        var n = !1;
        try {
          var o = Object.defineProperty({}, 'passive', {
            get: function () {
              n = !0;
            },
          });
          window.addEventListener('testPassive', null, o), window.removeEventListener('testPassive', null, o);
        } catch (e) {}
        function i() {
          var t = 'portrait' === a();
          return window.matchMedia('(max-device-'.concat(t ? 'width' : 'height', ': ').concat(e.options.mobilefriendly.breakpoint, 'px)'))
            .matches;
        }
        function a() {
          return 'orientation' in window.screen && 'type' in window.screen.orientation
            ? window.screen.orientation.type.replace(/\-\w+$/, '')
            : window.matchMedia('(orientation: portrait)').matches
            ? 'portrait'
            : 'landscape';
        }
        function l() {
          'portrait' === a()
            ? ((e.options.numberOfMonths = 1), (e.options.numberOfColumns = 1))
            : ((e.options.numberOfMonths = 2), (e.options.numberOfColumns = 2));
        }
        var c = function (t) {
            var n = t.touches[0];
            (e.xTouchDown = n.clientX), (e.yTouchDown = n.clientY);
          },
          s = function (t) {
            if (e.xTouchDown && e.yTouchDown) {
              var n = t.touches[0].clientX,
                o = t.touches[0].clientY,
                r = e.xTouchDown - n,
                i = e.yTouchDown - o,
                a = Math.abs(r) > Math.abs(i),
                l = e.options.numberOfMonths,
                c = null,
                s = !1,
                u = '',
                d = Array.from(e.ui.querySelectorAll('.month-item'));
              if (a) {
                var p = e.DateTime(e.ui.querySelector('.day-item').dataset.time),
                  f = Number(''.concat(1 - Math.abs(r) / 100)),
                  m = 0;
                if (r > 0) {
                  (m = -Math.abs(r)), (c = p.clone().add(l, 'month'));
                  var b = e.options.maxDate;
                  (s = !b || c.isSameOrBefore(e.DateTime(b), 'month')), (u = 'next');
                } else {
                  (m = Math.abs(r)), (c = p.clone().subtract(l, 'month'));
                  var h = e.options.minDate;
                  (s = !h || c.isSameOrAfter(e.DateTime(h), 'month')), (u = 'prev');
                }
                s &&
                  d.map(function (e) {
                    (e.style.opacity = f), (e.style.transform = 'translateX('.concat(m, 'px)'));
                  });
              }
              Math.abs(r) + Math.abs(i) > 100 && a && c && s && ((e.touchTargetMonth = u), e.gotoDate(c));
            }
          },
          u = function (t) {
            e.touchTargetMonth ||
              Array.from(e.ui.querySelectorAll('.month-item')).map(function (e) {
                (e.style.transform = 'translateX(0px)'), (e.style.opacity = 1);
              });
            (e.xTouchDown = null), (e.yTouchDown = null);
          };
        (e.backdrop = document.createElement('div')),
          (e.backdrop.className = 'litepicker-backdrop'),
          e.backdrop.addEventListener('click', e.hide()),
          t.element && t.element.parentNode && t.element.parentNode.appendChild(e.backdrop),
          window.addEventListener('orientationchange', function (n) {
            window.addEventListener('resize', function n() {
              if (i() && e.isShowning()) {
                var o = a();
                switch (o) {
                  case 'landscape':
                    (t.numberOfMonths = 2), (t.numberOfColumns = 2);
                    break;
                  default:
                    (t.numberOfMonths = 1), (t.numberOfColumns = 1);
                }
                e.ui.classList.toggle('mobilefriendly-portrait', 'portrait' === o),
                  e.ui.classList.toggle('mobilefriendly-landscape', 'landscape' === o),
                  e.render();
              }
              window.removeEventListener('resize', n);
            });
          }),
          t.inlineMode && i() && (window.dispatchEvent(new Event('orientationchange')), window.dispatchEvent(new Event('resize'))),
          e.on('before:show', function (t) {
            if (((e.triggerElement = t), !e.options.inlineMode && i())) {
              e.emit('mobilefriendly.before:show', t),
                (e.ui.style.position = 'fixed'),
                (e.ui.style.display = 'block'),
                l(),
                e.scrollToDate(t),
                e.render();
              var n = a();
              e.ui.classList.add('mobilefriendly'),
                e.ui.classList.toggle('mobilefriendly-portrait', 'portrait' === n),
                e.ui.classList.toggle('mobilefriendly-landscape', 'landscape' === n),
                (e.ui.style.top = '50%'),
                (e.ui.style.left = '50%'),
                (e.ui.style.right = null),
                (e.ui.style.bottom = null),
                (e.ui.style.zIndex = e.options.zIndex),
                (e.backdrop.style.display = 'block'),
                (e.backdrop.style.zIndex = e.options.zIndex - 1),
                document.body.classList.add('litepicker-open'),
                (t || e.options.element).blur(),
                e.emit('mobilefriendly.show', t);
            } else i() && (l(), e.render());
          }),
          e.on('render', function (t) {
            e.touchTargetMonth &&
              Array.from(e.ui.querySelectorAll('.month-item')).map(function (t) {
                return t.classList.add('touch-target-'.concat(e.touchTargetMonth));
              });
            e.touchTargetMonth = null;
          }),
          e.on('hide', function () {
            document.body.classList.remove('litepicker-open'),
              (e.backdrop.style.display = 'none'),
              e.ui.classList.remove('mobilefriendly', 'mobilefriendly-portrait', 'mobilefriendly-landscape');
          }),
          e.on('destroy', function () {
            e.backdrop && e.backdrop.parentNode && e.backdrop.parentNode.removeChild(e.backdrop);
          }),
          e.ui.addEventListener('touchstart', c, !!n && { passive: !0 }),
          e.ui.addEventListener('touchmove', s, !!n && { passive: !0 }),
          e.ui.addEventListener('touchend', u, !!n && { passive: !0 });
      },
    });
  },
  function (e, t, n) {
    e.exports = {
      litepickerBackdrop: 'litepicker-backdrop',
      litepickerOpen: 'litepicker-open',
      litepicker: 'litepicker',
      mobilefriendly: 'mobilefriendly',
      mobilefriendlyPortrait: 'mobilefriendly-portrait',
      mobilefriendlyLandscape: 'mobilefriendly-landscape',
      containerMonths: 'container__months',
      monthItemHeader: 'month-item-header',
      containerDays: 'container__days',
      monthItem: 'month-item',
      touchTargetNext: 'touch-target-next',
      lpBounceTargetNext: 'lp-bounce-target-next',
      touchTargetPrev: 'touch-target-prev',
      lpBounceTargetPrev: 'lp-bounce-target-prev',
    };
  },
]);
