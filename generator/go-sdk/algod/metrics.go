package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// Metrics /metrics
// Return metrics about algod functioning.
type Metrics struct {
	c *Client
}

// Do performs HTTP request
func (s *Metrics) Do(ctx context.Context,
	headers ...*common.Header) (response models.String, err error) {
	err = s.c.get(ctx, &response,
		"/metrics", nil, headers)
	return
}
